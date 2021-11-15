package org.jarvis.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jarvis.io.StreamCopyUtils;
import org.jarvis.json.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class DynamicResponseHandler<T> implements ResponseHandler<Response<T>> {

    private final Class<T> responseType;

    public DynamicResponseHandler(Class<T> responseType) {
        this.responseType = responseType;
    }

    @Override
    public Response<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        Headers headers = handleHeaders(response.getAllHeaders());
        T data = entity == null ? null : handleEntity(entity);
        return Response.<T>builder()
                .httpStatusCode(response.getStatusLine().getStatusCode())
                .header(headers)
                .data(data)
                .build();
    }

    private Headers handleHeaders(Header[] headers) {
        Headers convertHeaders = new Headers();
        Arrays.stream(headers).forEach(header -> convertHeaders.addHeader(header.getName(), header.getValue()));
        return convertHeaders;
    }

    @SuppressWarnings("unchecked")
    private T handleEntity(HttpEntity entity) throws IOException {
        if (responseType == String.class) {
            return (T) EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } else if (responseType == byte[].class) {
            return (T) StreamCopyUtils.copyToByteArray(entity.getContent());
        } else if (responseType == InputStream.class) {
            return (T) new ByteArrayInputStream(StreamCopyUtils.copyToByteArray(entity.getContent()));
        } else if (ContentType.APPLICATION_JSON.getMimeType().equals(ContentType.get(entity).getMimeType())) {
            return JsonUtils.toObj(EntityUtils.toString(entity, StandardCharsets.UTF_8), responseType);
        } else {
            throw new IllegalArgumentException("http response转换异常");
        }
    }
}
