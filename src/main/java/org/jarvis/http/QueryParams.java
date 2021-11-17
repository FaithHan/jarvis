package org.jarvis.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class QueryParams extends HashMap<String, List<String>> {

    public QueryParams addParam(String paramName, String value) {
        computeIfAbsent(paramName, key -> new ArrayList<>()).add(value);
        return this;
    }

    public QueryParams addParams(QueryParams params) {
        params.forEach((paramName, paramValues) -> computeIfAbsent(paramName, key -> new ArrayList<>()).addAll(paramValues));
        return this;
    }

    /**
     * get first param
     *
     * @param paramName
     * @return
     */
    public String getParam(String paramName) {
        List<String> params = getParams(paramName);
        return params.isEmpty() ? null : params.get(0);
    }

    public List<String> getParams(String paramName) {
        return Optional.ofNullable(get(paramName)).orElse(Collections.emptyList());
    }

    /**
     * clear param by name
     *
     * @param paramName
     */
    public void clear(String paramName) {
        super.remove(paramName);
    }

    @Override
    public void clear() {
        super.clear();
    }
}
