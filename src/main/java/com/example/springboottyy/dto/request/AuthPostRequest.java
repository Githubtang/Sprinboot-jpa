package com.example.springboottyy.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 1:45
 * @Version: 1.0
 */
@Getter
@Setter
public class AuthPostRequest {
    private Long userId;
    private List<Long> postIds;
}
