package com.example.springboottyy.exception.user;

/**
 * @Author: GithubTang
 * @Description: TODO
 * @Date: 2025/3/8 22:54
 * @Version: 1.0
 */
public class UserPasswordRetryLimitExceedException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime)
    {
        super("user.password.retry.limit.exceed", new Object[] { retryLimitCount, lockTime });
    }
}
