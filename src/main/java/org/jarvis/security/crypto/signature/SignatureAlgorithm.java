package org.jarvis.security.crypto.signature;


public class SignatureAlgorithm {

    public static final String RS_256 = "SHA256withRSA";

    /**
     * https://en.wikipedia.org/wiki/PKCS_1
     * https://blog.csdn.net/chenzhi5174/article/details/100719023
     * https://www.cryptosys.net/pki/manpki/pki_rsaschemes.html
     * http://web.archive.org/web/20040713140300/http://grouper.ieee.org/groups/1363/P1363a/contributions/pss-submission.pdf
     */
    public static final String RS_256_PSS = "SHA256withRSA/PSS";

    public static final String RS_384 = "SHA384withRSA";

    public static final String RS_521 = "SHA512withRSA";


}
