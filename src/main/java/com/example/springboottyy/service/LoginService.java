package com.example.springboottyy.service;

import com.example.springboottyy.common.constant.Constants;
import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.exception.user.UserPasswordNotMatchException;
import com.example.springboottyy.manager.AsyncManager;
import com.example.springboottyy.manager.factory.AsyncFactory;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.security.contest.AuthenticationContextHolder;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.DateUtils;
import com.example.springboottyy.utils.JwtUtil;
import com.example.springboottyy.utils.MessageUtils;
import com.example.springboottyy.utils.ip.IpUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.rmi.ServerException;
import java.time.ZoneId;

/**
 * @Author: Insight
 * @Description: 登录校验方法
 * @Date: 2024/10/15 8:11
 * @Version: 1.0
 */
@Component
public class LoginService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SysPasswordService passwordService;

    public ApiResponse<?> login(String username, String password) {
        // 登录前校验
        loginPreCheck(username, password);
        String ip = IpUtils.getIpAddr();
        Authentication authenticate = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用CustomUserDetailsService.loadUserByUsername
            authenticate = authenticationManager.authenticate(authenticationToken);
            // 生成 token
            LoginUser user = (LoginUser) authenticate.getPrincipal();
            recordLoginInfo(user.getId());
            String token = jwtUtil.createToken(user);
            /*
             * 删除token version : 1.0
             * String token = jwtUtil.generateToken(user.getUsername());
             */
            return ApiResponse.success("success", token);
        } catch (BadCredentialsException e) {
            /*
             * version : 1.0
             * return ApiResponse.error("Invalid username or password", null);
             */
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        } catch (Exception e) {
            /*
             * version : 1.0
             * return ApiResponse.error(e.getMessage(), e);
             */
            passwordService.incrementIpFailCount(ip);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
            throw new ServiceException(e.getMessage());
        } finally {
            AuthenticationContextHolder.clearContext();
        }
    }

    /* 登录前校验 */
    public void loginPreCheck(String username, String password) {
        // username or password is null
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * TODO 验证码校验
     */
    public void validateCaptcha(String username, String code, String uuid) {
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime()); // 这里吧data转为数据库的LocalDateTime
    }
}
