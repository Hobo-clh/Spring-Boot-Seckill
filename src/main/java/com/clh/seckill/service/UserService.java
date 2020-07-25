package com.clh.seckill.service;

import com.clh.seckill.dto.UserDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.exception.GlobleException;
import com.clh.seckill.mapper.UserMapper;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.UserKey;
import com.clh.seckill.util.MD5Util;
import com.clh.seckill.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Service
public class UserService {
    public static final String COOKIE_NAME_TOKEN = "token";

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisService redisService;

    public User getByToken(String token, HttpServletResponse response) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 通过id查询用户
     * <p>使用缓存 + 数据库</p>
     *
     * @param id
     * @return
     */
    public User findById(Long id) {
        //取缓存
        User user = redisService.get(UserKey.getById, String.valueOf(id), User.class);
        if (user != null) {
            return user;
        }
        //缓存中没有就取数据库
        User dbUser = userMapper.findById(id);
        redisService.set(UserKey.getById, "" + id, dbUser);
        return dbUser;
    }

    /**
     * 更改用户密码
     * @param token
     * @param id
     * @param passwordNew
     * @return
     */
    public boolean updatePwd(String token, Long id, String passwordNew) {
        //取缓存
        User user = redisService.get(UserKey.getById, String.valueOf(id), User.class);
        if (user == null) {
            throw new GlobleException(CodeMsgEnum.MOBILE_NOT_EXIST);
        }
        //缓存中没有就取数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew, user.getSalt()));
        userMapper.updatePwd(toBeUpdate);
        //处理缓存
        redisService.delete(UserKey.getById, String.valueOf(id));
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(UserKey.token, token, user);
        return true;
    }


    public String login(UserDTO userDTO, HttpServletResponse response) {

        if (userDTO == null) {
            throw new GlobleException(CodeMsgEnum.SERVER_ERROR);
        }
        String mobile = userDTO.getMobile();
        String password = userDTO.getPassword();
        //
        User user = findById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobleException(CodeMsgEnum.MOBILE_NOT_EXIST);
        }
        String calPass = MD5Util.formPassToDBPass(password, user.getSalt());
        if (!calPass.equals(user.getPassword())) {
            throw new GlobleException(CodeMsgEnum.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    /**
     * 将token添加至cookie和redis中
     *
     * @param response
     * @param token    用户标识符token
     * @param user
     */
    public void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        //设置后 cookie可以在同一应用服务器内共享
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
