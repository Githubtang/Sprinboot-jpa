package com.example.springboottyy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 1:44
 * @Version: 1.0
 */
@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

}
