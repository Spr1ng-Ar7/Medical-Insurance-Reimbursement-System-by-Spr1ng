package org.neu.insurance.util;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT工具类
 */
@Slf4j
public class JwtUtil {
    
    private static final String SECRET_KEY = "neuInsuranceSecretKey2024";
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24小时
    
    /**
     * 从token中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        if (token == null || token.isEmpty()) {
            log.error("token为空");
            return null;
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            
            String username = claims.getSubject();
            log.info("从token中获取用户名：{}", username);
            return username;
        } catch (Exception e) {
            log.error("JWT解析失败：{}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 检查token是否过期
     */
    public static boolean isTokenExpired(String token) {
        if (token == null || token.isEmpty()) {
            return true;
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("检查token过期失败：{}", e.getMessage());
            return true;
        }
    }
    
    /**
     * 验证token
     */
    public static boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证JWT签名和过期时间
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("JWT验证失败：{}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 删除token
     */
    public static void deleteToken(String token) {
        // TODO: 实现token删除逻辑
        // 可以将token加入黑名单或从缓存中删除
        if (token == null || token.isEmpty()) {
            return;
        }
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        claims.setExpiration(new Date(0));
        Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        log.info("token已删除");
        return;
    }
    
    /**
     * 生成JWT token
     */
    public static String generateToken(String username, Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .addClaims(claims != null ? claims : new HashMap<>())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    
    /**
     * 生成JWT token（简化版）
     */
    public static String generateToken(String username) {
        return generateToken(username, null);
    }
    
    /**
     * 从token中获取过期时间
     */
    public static Date getExpirationFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("获取token过期时间失败：{}", e.getMessage());
            return null;
        }
    }
} 