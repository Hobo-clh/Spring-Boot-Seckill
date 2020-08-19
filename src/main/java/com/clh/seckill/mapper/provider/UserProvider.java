package com.clh.seckill.mapper.provider;

import com.clh.seckill.model.User;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author: LongHua
 * @date: 2020/8/19
 **/
public class UserProvider {
    public String updateUser(User user) {
        return new SQL() {
            {
                UPDATE("user");
                if (user.getNickname() != null) {
                    SET("nickname = #{nickname}");
                }
                if (user.getHead() != null) {
                    SET("head = #{head}");
                }
                if (user.getLastLoginDate() != null) {
                    SET("last_login_time = #{lastLoginDate}");
                }
                if (user.getLoginCount() != null) {
                    SET("login_count = login_count + #{loginCount}");
                }
                if (user.getEmail() != null) {
                    SET("email =  #{email}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }

}
