package org.jarvis.http;

import lombok.SneakyThrows;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <b>Which Java HTTP client should I use in 2020?<b/><br>
 * <p>https://www.mocklab.io/blog/which-java-http-client-should-i-use-in-2020/</p>
 * <br
 * <b>Apache Httpclient4 maven repository<b/><br>
 * <p>https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
 *
 * <p> tutorial @see https://www.baeldung.com/httpclient-guide
 */
public class HttpSite implements Closeable {

    private final CloseableHttpClient client;

    /**
     * use HttpSiteFactory to create HttpSite
     *
     * @param client
     */
    HttpSite(CloseableHttpClient client) {
        this.client = client;
    }

    @SneakyThrows
    public <T> Response<T> get(String uri, Class<T> responseType) {
        return execute(Request.get(uri).build(), responseType);
    }

    @SneakyThrows
    public <T> Response<T> get(String uri, QueryParams queryParams, Headers headers, Class<T> responseType) {
        return execute(Request.get(uri).headers(headers).queryParams(queryParams).build(), responseType);
    }

    @SneakyThrows
    public <R, T> Response<T> post(String uri, R body, Class<T> responseType) {
        return execute(Request.post(uri).build(body), responseType);
    }

    @SneakyThrows
    public <R, T> Response<T> post(String uri, Headers headers, R body, Class<T> responseType) {
        return execute(Request.post(uri).headers(headers).build(body), responseType);
    }

    @SneakyThrows
    public <T> Response<T> delete(String uri, Class<T> responseType) {
        return execute(Request.delete(uri).build(), responseType);
    }

    @SneakyThrows
    public <T> Response<T> delete(String uri, QueryParams queryParams, Headers headers, Class<T> responseType) {
        return execute(Request.delete(uri).headers(headers).queryParams(queryParams).build(), responseType);
    }

    @SneakyThrows
    public <R, T> Response<T> put(String uri, R body, Class<T> responseType) {
        return execute(Request.put(uri).build(body), responseType);
    }

    @SneakyThrows
    public <R, T> Response<T> put(String uri, Headers headers, R body, Class<T> responseType) {
        return execute(Request.put(uri).headers(headers).build(body), responseType);
    }

    /**
     * do execute
     *
     * @param request
     * @param responseType
     * @param <R>
     * @param <T>
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public <R, T> Response<T> execute(Request<R> request, Class<T> responseType) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(request.getUri());
        RequestUtils.addParamForUriBuilder(uriBuilder, request.getQueryParams());
        URI uri = uriBuilder.build();

        Headers headers = request.getHeaders();
        HttpMethod httpMethod = request.getHttpMethod();
        switch (httpMethod) {
            case GET:
                HttpGet httpGet = new HttpGet(uri);
                RequestUtils.addHeadersForHttpMessage(httpGet, headers);
                return client.execute(httpGet, new DynamicResponseHandler<>(responseType));
            case DELETE:
                HttpDelete httpDelete = new HttpDelete(uri);
                RequestUtils.addHeadersForHttpMessage(httpDelete, headers);
                return client.execute(httpDelete, new DynamicResponseHandler<>(responseType));
            case POST:
                HttpPost httpPost = new HttpPost(uri);
                RequestUtils.addHeadersForHttpMessage(httpPost, headers);
                httpPost.setEntity(RequestUtils.resolveEntity(request.getRequestBody()));
                return client.execute(httpPost, new DynamicResponseHandler<>(responseType));
            case PUT:
                HttpPut httpPut = new HttpPut(uri);
                RequestUtils.addHeadersForHttpMessage(httpPut, headers);
                httpPut.setEntity(RequestUtils.resolveEntity(request.getRequestBody()));
                return client.execute(httpPut, new DynamicResponseHandler<>(responseType));
            default:
                throw new IllegalArgumentException(String.format("%s http method is not supported", httpMethod.name()));
        }
    }


    @Override
    public void close() throws IOException {
        client.close();
    }
}


