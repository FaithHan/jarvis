package org.jarvis.http;

import java.util.ArrayList;
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
}
