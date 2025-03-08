package com.example.springboottyy.service;

import com.example.springboottyy.common.constant.CacheConstants;
import com.example.springboottyy.common.constant.Constants;
import com.example.springboottyy.exception.user.IpRetryLimitExceedException;
import com.example.springboottyy.exception.user.UserPasswordNotMatchException;
import com.example.springboottyy.exception.user.UserPasswordRetryLimitExceedException;
import com.example.springboottyy.manager.AsyncManager;
import com.example.springboottyy.manager.factory.AsyncFactory;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.security.contest.AuthenticationContextHolder;
import com.example.springboottyy.utils.CacheUtils;
import com.example.springboottyy.utils.MessageUtils;
import com.example.springboottyy.utils.SecurityUtils;
import com.example.springboottyy.utils.ip.IpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: GithubTang
 * @Description: 登录密码方法
 * @Date: 2025/3/8 1:57
 * @Version: 1.0
 */
@Component
public class SysPasswordService {

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    @Value(value = "${user.ip.maxRetryCount:15}")
    public int maxIpRetryCount;

    @Value(value = "${user.ip.lockTime:15}")
    public int ipLockTime;
    /**
     * 登录账户密码错误次数缓存键名
     *
     * @return 缓存Cache
     */
    private Cache getCache()
    {
        return CacheUtils.getCache(CacheConstants.PWD_ERR_CNT_KEY);
    }

    private Cache getIpCache() {
        return CacheUtils.getCache(CacheConstants.IP_ERR_CNT_KEY);
    }

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        String ip = IpUtils.getIpAddr();
        validateIp(ip);
        Integer retryCount = getCache().get(username, Integer.class);
        if (retryCount == null)
        {
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime)));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }
        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.count", retryCount)));
            CacheUtils.put(CacheConstants.PWD_ERR_CNT_KEY,username,retryCount,lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public void validateIp(String ip)
    {
        Integer ipRetryCount = getIpCache().get(ip, Integer.class);
        if (ipRetryCount == null)
        {
            ipRetryCount = 0;
        }

        if (ipRetryCount >= maxIpRetryCount)
        {
            throw new IpRetryLimitExceedException(maxIpRetryCount, ipLockTime);
        }
    }

    public void incrementIpFailCount(String ip)
    {
        Integer ipRetryCount = getIpCache().get(ip, Integer.class);
        if (ipRetryCount == null)
        {
            ipRetryCount = 0;
        }
        ipRetryCount += 1;
        CacheUtils.put(CacheConstants.IP_ERR_CNT_KEY,ip,ipRetryCount,ipLockTime,TimeUnit.MINUTES);
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        getCache().evictIfPresent(loginName);
    }
}
