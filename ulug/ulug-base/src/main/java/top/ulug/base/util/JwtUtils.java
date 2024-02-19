package top.ulug.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * Created by liujf on 2021/6/13.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class JwtUtils {

    public static final String secretString = "CHYN1111999988888888ULUGCHYN2222000022221111ULUG";

    /**
     * 创建jws
     *
     * @param id      id
     * @param subject 主题
     * @return
     */
    public static String create(String id, String subject) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
        Date now = new Date();
        String jws = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (24 * 60 * 60 * 1000)))
                .signWith(key)
                .compact();
        return Base64.getEncoder().encodeToString(jws.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解析
     *
     * @param token token
     * @return claims
     * @throws Exception
     */
    public static Claims pares(String token) throws Exception {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(new String(Base64.getDecoder().decode(token)))
                .getBody();
        return claims;
    }
}
