package com.example.springboottyy.exception.user;

/**
 * @Author: GithubTang
 * @Description: 用户密码不正确或不符合规范异常类
 * @Date: 2025/3/8 22:57
 * @Version: 1.0
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException()
    {
        super("user.password.not.match", null);
    }
}
