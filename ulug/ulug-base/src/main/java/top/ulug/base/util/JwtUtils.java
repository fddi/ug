package top.ulug.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Created by liujf on 2021/6/13.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class JwtUtils {

    public static final String secretString = "JWTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT";

    /**
     * 创建jws
     *
     * @param id      id
     * @param subject 主题
     * @return
     */
    public static String create(String id, String subject, long overtime) {
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusSeconds(overtime));
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
        Date now = new Date();
        String jws = Jwts.builder()
                .id(id)
                .subject(subject)
                .issuedAt(now)
                .expiration(exprireDate)
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
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(new String(Base64.getDecoder().decode(token))).getPayload();
        return claims;
    }
}
