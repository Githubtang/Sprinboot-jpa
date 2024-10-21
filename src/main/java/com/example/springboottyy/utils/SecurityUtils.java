package com.example.springboottyy.utils;

import com.example.springboottyy.common.constant.HttpStatus;
import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/14 8:19
 * @Version: 1.0
 */
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 对象转换
     */

    /**
     * 获取用户名
     */
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        try {
            return getLoginUser().getId();
        } catch (Exception e) {
            throw  new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }
}

