package com.clh.seckill.dto;

import com.clh.seckill.validator.IsMobile;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Data
public class UserDTO {
    @NotBlank
    @IsMobile
    private String mobile;

    @NotBlank
    private String password;
}
