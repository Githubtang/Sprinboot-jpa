package com.example.springboottyy.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 1:49
 * @Version: 1.0
 */
@Getter
@Setter
public class AuthMenuRequest {
    private Long roleId;
    private Set<Long> menuIds;
}
