package com.example.springboottyy.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 1:45
 * @Version: 1.0
 */
@Getter
@Setter
public class AuthRoleRequest {
    private Long userId;
    private Long roleId;
}
