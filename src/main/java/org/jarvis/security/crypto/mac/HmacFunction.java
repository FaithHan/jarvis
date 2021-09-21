package org.jarvis.security.crypto.mac;

/**
 * @see <a href="http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider">
 *      Java 6 Cryptography Architecture Sun Providers Documentation</a>
 * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#SunJCEProvider">
 *      Java 7 Cryptography Architecture Sun Providers Documentation</a>
 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html#SunJCEProvider">
 *      Java 8 Cryptography Architecture Sun Providers Documentation</a>
 * @see <a href=
 *      "http://docs.oracle.com/javase/9/security/oracleproviders.htm#JSSEC-GUID-A47B1249-593C-4C38-A0D0-68FA7681E0A7">
 *      Java 9 Cryptography Architecture Sun Providers Documentation</a>
 */
public enum HmacFunction {

    HMAC_MD5("HmacMD5"),

    HMAC_SHA_1("HmacSHA1"),

    HMAC_SHA_224("HmacSHA224"),

    HMAC_SHA_256("HmacSHA256"),

    HMAC_SHA_384("HmacSHA384"),

    HMAC_SHA_512("HmacSHA512");

    private final String name;

    HmacFunction(final String algorithm) {
        this.name = algorithm;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}