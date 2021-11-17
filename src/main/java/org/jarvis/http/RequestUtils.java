package org.jarvis.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.jarvis.json.JsonUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

abstract class RequestUtils {

    public static void addHeadersForHttpMessage(AbstractHttpMessage httpMessage, Headers headers) {
        if (headers == null) {
            return;
        }
        headers.forEach((headerName, headerValues) -> headerValues.forEach(headerValue -> httpMessage.addHeader(headerName, headerValue)));
    }

    public static void addParamForUriBuilder(URIBuilder uriBuilder, QueryParams queryParams) {
        if (queryParams == null) {
            return;
        }
        queryParams.forEach((paramName, paramValues) -> paramValues.forEach(paramValue -> uriBuilder.addParameter(paramName, paramValue)));

    }

    public static HttpEntity resolveEntity(Object requestBody) {
        if (requestBody == null) {
            return null;
        }
        if (requestBody instanceof String) {
            return new StringEntity((String) requestBody, ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));
        } else if (requestBody instanceof MultipartEntity) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            MultipartEntity entity = (MultipartEntity) requestBody;
            entity.getTextMap().forEach((name, values) -> {
                values.forEach(value -> builder.addTextBody(name, value, ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            });
            entity.getBinaryMap().forEach((name, values) -> {
                for (Object value : values) {
                    if (value instanceof File) {
                        builder.addBinaryBody(name, (File) value, ContentType.APPLICATION_OCTET_STREAM, null);
                    } else if (value instanceof InputStream) {
                        builder.addBinaryBody(name, (InputStream) value, ContentType.APPLICATION_OCTET_STREAM, null);
                    } else if (value instanceof byte[]) {
                        builder.addBinaryBody(name, (byte[]) value, ContentType.APPLICATION_OCTET_STREAM, null);
                    }
                }
            });
            return builder.build();
        } else if (requestBody instanceof FormEntity) {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            FormEntity formEntity = (FormEntity) requestBody;
            formEntity.forEach((paramName, paramValues) ->
                    paramValues.forEach(paramValue -> nameValuePairs.add(new BasicNameValuePair(paramName, paramValue))));
            return new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8);
        } else {
            return new StringEntity(JsonUtils.toJson(requestBody), ContentType.APPLICATION_JSON);
        }
    }


}
