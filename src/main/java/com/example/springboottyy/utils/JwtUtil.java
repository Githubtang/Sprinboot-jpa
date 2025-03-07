package com.example.springboottyy.utils;

import com.example.springboottyy.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @Author: Insight
 * @Description: token验证处理
 * @Date: 2024/10/12 23:56
 * @Version: 1.0
 */
@Component
@ConfigurationProperties(prefix = "token")
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
//    private String SECRET_KEY = "Y2M1ZTI4MDM4ZTQwNGJmYjE5MjEyZTBlZDYxNzEyMjk4MjE1MjMzYzdhN2YzZTc1OTQ2YzEy";
    private String SECRET_KEY;
//    @Value("${token.expireTime}")
    private long TOKEN_VALIDITY;

    private String HEADER;
    @Autowired
    private HttpServletRequest request;

    private final RedisCache redisCache;

    public JwtUtil(RedisCache redisCache) {
        this.redisCache = redisCache;
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

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
