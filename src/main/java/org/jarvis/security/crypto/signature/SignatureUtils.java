package org.jarvis.security.crypto.signature;

import org.jarvis.codec.Base64Utils;
import org.jarvis.security.crypto.RSAUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 *  数字签名工具类
 */
public class SignatureUtils {

    private static String algorithm = SignatureFunction.RS_256;

    private static PublicKey publicKey;

    private static PrivateKey privateKey;

    public static byte[] sign(byte[] contentBytes) {
        try {
            Signature s = Signature.getInstance(algorithm);
            s.initSign(privateKey);
            s.update(contentBytes);
            return s.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(byte[] contentBytes, byte[] signatureBytes) {
        try {
            Signature s = Signature.getInstance(algorithm);
            s.initVerify(publicKey);
            s.update(contentBytes);
            return s.verify(signatureBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static void config(String algorithm) {
        if (algorithm != null) {
            SignatureUtils.algorithm = algorithm;
        }
    }

    public static void config(PublicKey publicKey, PrivateKey privateKey) {
        if (publicKey != null && privateKey != null) {
            SignatureUtils.publicKey = publicKey;
            SignatureUtils.privateKey = privateKey;
        }
    }

    static {
        String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKtkMsHtkYC4eyWcC9QOFVXFgUUAZmW" +
                "lpJZ/MZ/n8BeMBpkZd6F9w9a8twKnhDxL97N8mUX674cA8Al/WhQVX35Y9gOwg9m0Vg6KHDlzEpQ7/UBfiXt6R" +
                "SNHptl4Sx0/5bLQffQqrgEWwCOrG9mzWqrw/yXDz625Wnw5gYu9W+BQIDAQAB";

        String privateKeyString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMq2Qywe2RgLh7JZwL1A4" +
                "VVcWBRQBmZaWkln8xn+fwF4wGmRl3oX3D1ry3AqeEPEv3s3yZRfrvhwDwCX9aFBVfflj2A7CD2bRWDoocOXMSlDv" +
                "9QF+Je3pFI0em2XhLHT/lstB99CquARbAI6sb2bNaqvD/JcPPrblafDmBi71b4FAgMBAAECgYEAmlOkVCSwFpRAn" +
                "NGj4PFSG9CP5fb+yFGlRxlyhKmyO4aYxkZnUjZ0H/a/DQfoQ3+4X7CbMPCfntiBM2x9PmUlcpaudEucqQ7Jdfprwi" +
                "HfMeNMZDHFuCGcmvij0Vwpqe+9HK+rR6Ry2iwGTEhHlMGgd+WyHUuK7EeO5pdAcdj0AokCQQD7b33RCM/Hf3RGhBGX" +
                "I92NzYDbid55ne4SSEgd2B5HZW7P8/qNaCdFGe92PYwbCJaOjNeeCNTvLoq67pDVlwWDAkEAzmRV6OZlrAbWFrtf5dY" +
                "ru4mtl2xNZQlQEMHSO2aKFKcP0v7mwGRgksUwJ3MhjtgX3IcnHDNEO/TEEusuD5Hf1wJAbIshAZCQQktfYuG4xkqRZp" +
                "oEusG17UKkd/gESf32dFPxlox46XrBGS+tFJpVVYZLmJZsrqiZY/fvo+tBUzOzCQJAOm2LKJC47bdNuke3QUCdRs0WG" +
                "IzvaA/325aTJn/DzU7yGuBQgNypvkMWe8SLFn40WxjjyIb25SZIr7ZmZr/8JwJBAMI3Ce06T1ArCp0Ex9Sbir87EoyU" +
                "Nkn1tnQBqYflt4WvEY1JtLD7+xQmw2g7AZRJjwhlxfN0wmPDTDEItwGpKq4=";

        publicKey = RSAUtils.toPublicKey(Base64Utils.decode(publicKeyString));

        privateKey = RSAUtils.toPrivateKey(Base64Utils.decode(privateKeyString));
    }
}
