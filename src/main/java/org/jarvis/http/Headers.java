package org.jarvis.http;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Headers extends HashMap<String, List<String>> {

    public Headers addHeader(String headerName, String value) {
        computeIfAbsent(headerName, key -> new ArrayList<>()).add(value);
        return this;
    }

    public Headers addHeaders(Headers headers) {
        headers.forEach((headerName, headerValues) -> computeIfAbsent(headerName, key -> new ArrayList<>()).addAll(headerValues));
        return this;
    }

    public Headers addBasicAuth(String username, String password) {
        return addHeader("Authorization", "Basic " + encodeBasicAuth(username, password, StandardCharsets.UTF_8));
    }

    /**
     * get first header
     *
     * @param headerName
     * @return
     */
    public String getHeader(String headerName) {
        List<String> headers = getHeaders(headerName);
        return headers.isEmpty() ? null : headers.get(0);
    }

    public List<String> getHeaders(String headerName) {
        return Optional.ofNullable(get(headerName)).orElse(Collections.emptyList());
    }

    /**
     * clear header by name
     *
     * @param headerName
     */
    public void clear(String headerName) {
        super.remove(headerName);
    }

    @Override
    public void clear() {
        super.clear();
    }

    private static String encodeBasicAuth(String username, String password, @Nullable Charset charset) {
        Assert.notNull(username, "Username must not be null");
        Assert.doesNotContain(username, ":", "Username must not contain a colon");
        Assert.notNull(password, "Password must not be null");
        if (charset == null) {
            charset = StandardCharsets.ISO_8859_1;
        }

        CharsetEncoder encoder = charset.newEncoder();
        if (!encoder.canEncode(username) || !encoder.canEncode(password)) {
            throw new IllegalArgumentException(
                    "Username or password contains characters that cannot be encoded to " + charset.displayName());
        }

        String credentialsString = username + ":" + password;
        byte[] encodedBytes = Base64.getEncoder().encode(credentialsString.getBytes(charset));
        return new String(encodedBytes, charset);
    }
}
