package com.example.springboottyy.exception.user;

import com.example.springboottyy.exception.base.BaseException;

/**
 * @Author: GithubTang
 * @Description: 用户信息异常类
 * @Date: 2025/3/8 22:55
 * @Version: 1.0
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
