package com.clh.seckill.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Data
public class User {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
