package org.jarvis.security.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.jarvis.date.LocalDateTimeUtils;
import org.jarvis.security.crypto.RSAUtils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public abstract class JWTService {

    private static final Long DEFAULT_EXPIRE_MINUTE = 60L;

    private static final String DEFAULT_HMAC_SECRET = "qazWSX123";

    private static final RSAKeyProvider RSA_KEY_PROVIDER = new RSAKeyProvider() {

        private final KeyPair keyPair = RSAUtils.getKeyPair();

        @Override
        public RSAPublicKey getPublicKeyById(String keyId) {
            return (RSAPublicKey) keyPair.getPublic();
        }

        @Override
        public RSAPrivateKey getPrivateKey() {
            return (RSAPrivateKey) keyPair.getPrivate();
        }

        @Override
        public String getPrivateKeyId() {
            return "default";
        }
    };

    public abstract String createToken(Object userInfo);

    public abstract Map<String, Claim> verifyToken(String token);

    public abstract <T> T getUserInfo(String token, Class<T> classValue);

    protected Map<String, Object> header(){
        return Collections.emptyMap();
    }

    protected Date expireTime() {
        return LocalDateTimeUtils.toDate(LocalDateTime.now().plusMinutes(DEFAULT_EXPIRE_MINUTE));
    }

    protected String secret() {
        return DEFAULT_HMAC_SECRET;
    }

    protected RSAKeyProvider rsaKeyProvider() {
        return RSA_KEY_PROVIDER;
    }

}
