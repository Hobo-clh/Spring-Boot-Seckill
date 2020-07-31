package com.clh.seckill.dto;

import com.clh.seckill.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotBlank
    @IsMobile
    private String mobile;

    @NotBlank
    private String password;
}
