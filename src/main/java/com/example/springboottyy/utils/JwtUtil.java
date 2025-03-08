package com.example.springboottyy.utils;

import com.example.springboottyy.common.constant.CacheConstants;
import com.example.springboottyy.common.constant.Constants;
import com.example.springboottyy.exception.TokenExpiredException;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.utils.ip.AddressUtils;
import com.example.springboottyy.utils.ip.IpUtils;
import com.example.springboottyy.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Insight
 * @Description: token验证处理
 * @Date: 2024/10/12 23:56
 * @Version: 1.0
 */
@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${token.secret_key}")
    private String SECRET_KEY;
    @Value("${token.expireTime}")
    private long TOKEN_VALIDITY;
    @Value("${token.header}")
    private String HEADER;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private HttpServletRequest request;

    private final RedisCache redisCache;

    public JwtUtil(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            try {
                Claims claims = extractClaims(token);
                // 提取对应的权限及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                return CacheUtils.get(CacheConstants.LOGIN_TOKEN_KEY, uuid, LoginUser.class);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void deleteLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            CacheUtils.remove(CacheConstants.LOGIN_TOKEN_KEY, token);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        Long expireTime = loginUser.getExpireTime();
        Long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime < MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + TOKEN_VALIDITY + MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        CacheUtils.put(CacheConstants.LOGIN_TOKEN_KEY, loginUser.getToken(), loginUser, TOKEN_VALIDITY, TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    /**
     * 从token中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if (token != null && token.startsWith(Constants.TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    /**
     * 从数据中生成token
     *
     * @param username 数据声明
     * @return token
     */
    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        String token = Jwts.builder()
                .subject(username)
                .signWith(key)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .compact();
        // 将token存入redis + timeout
        redisCache.setToken(token, username, TOKEN_VALIDITY);
        return token;
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }



    /**
     * 清除token
     *
     * @param token
     */
    public void invalidateToken(String token) {
        if (redisCache.hasToken(token)) {
            redisCache.delToken(token);
        }
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        if (!redisCache.hasToken(token)) {
            return false;
        }
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }



    public String refreshToken() throws TokenExpiredException {
        String token = getToken(request);
        if (isTokenExpired(token)) {
            throw new TokenExpiredException("token is expired");
        }
        String username = extractUsername(token);
        return generateToken(username);
    }

}
