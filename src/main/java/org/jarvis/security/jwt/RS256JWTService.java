package org.jarvis.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.NoArgsConstructor;
import org.jarvis.date.LocalDateTimeUtils;
import org.jarvis.json.JsonUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
public class RS256JWTService extends JWTService {

    private Long expireMinute;

    private RSAKeyProvider rsaKeyProvider;

    public RS256JWTService(PublicKey publicKey, PrivateKey privateKey, Long expireMinute) {
        this.rsaKeyProvider = new RSAKeyProvider() {
            @Override
            public RSAPublicKey getPublicKeyById(String keyId) {
                return (RSAPublicKey) publicKey;
            }

            @Override
            public RSAPrivateKey getPrivateKey() {
                return (RSAPrivateKey) privateKey;
            }

            @Override
            public String getPrivateKeyId() {
                return "custom";
            }
        };
        this.expireMinute = expireMinute;
    }

    @Override
    public String createToken(Object userInfo) {
        return JWT.create()
                .withHeader(header()) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP")
                .withClaim("user", JsonUtils.toJson(userInfo))
                .withIssuedAt(new Date()) // sign time
                .withExpiresAt(expireTime()) // expire time
                .sign(Algorithm.RSA256(rsaKeyProvider())); // signature
    }

    @Override
    public Map<String, Claim> verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(rsaKeyProvider())).build();
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
    protected RSAKeyProvider rsaKeyProvider() {
        return this.rsaKeyProvider != null ? this.rsaKeyProvider :super.rsaKeyProvider();
    }
}
