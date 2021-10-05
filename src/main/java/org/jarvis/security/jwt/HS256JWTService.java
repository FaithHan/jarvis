package org.jarvis.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jarvis.date.LocalDateTimeUtils;
import org.jarvis.json.JsonUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class HS256JWTService extends JWTService {

    private Long expireMinute;

    private String hmacSecret;

    @Override
    public String createToken(Object userInfo) {
        return JWT.create()
                .withHeader(header()) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP")
                .withClaim("user", JsonUtils.toJson(userInfo))
                .withIssuedAt(new Date()) // sign time
                .withExpiresAt(expireTime()) // expire time
                .sign(Algorithm.HMAC256(secret())); // signature
    }

    @Override
    public Map<String, Claim> verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret())).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T getUserInfo(String token, Class<T> classValue) {
        Map<String, Claim> claims = verifyToken(token);
        Claim claim = Objects.requireNonNull(claims).get("user");
        return JsonUtils.toObj(claim.asString(), classValue);
    }

    @Override
    protected Map<String, Object> header() {
        return super.header();
    }

    @Override
    protected Date expireTime() {
        if (expireMinute != null) {
            return LocalDateTimeUtils.toDate(LocalDateTime.now().plusMinutes(expireMinute));
        }
        return super.expireTime();
    }

    @Override
    protected String secret() {
        return hmacSecret != null ? hmacSecret : super.secret();
    }
}
