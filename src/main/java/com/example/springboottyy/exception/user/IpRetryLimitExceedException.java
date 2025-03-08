package com.example.springboottyy.exception.user;

/**
 * @Author: GithubTang
 * @Description: TODO
 * @Date: 2025/3/8 22:58
 * @Version: 1.0
 */
public class IpRetryLimitExceedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IpRetryLimitExceedException(int retryLimitCount, int lockTime)
    {
        super("失败次数过多,你的ip暂时被限制登录.");
    }
}
