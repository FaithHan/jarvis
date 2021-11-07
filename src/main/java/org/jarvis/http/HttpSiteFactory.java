package org.jarvis.http;

import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;

public class HttpSiteFactory {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36";

    private int timeout = 60;

    private HttpHost proxy;

    private boolean chromeMode;


    public static HttpSite create() {
        return custom().build();
    }

    public static HttpSiteFactory custom() {
        return new HttpSiteFactory();
    }

    /**
     * timeout(with second unit)
     *
     * @param timeout
     * @return
     */
    public HttpSiteFactory timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public HttpSiteFactory chromeMode() {
        this.chromeMode = true;
        return this;
    }

    public HttpSiteFactory proxy(String host, int port) {
        this.proxy = new HttpHost(host, port);
        return this;
    }

    public HttpSiteFactory proxy(int port) {
        this.proxy = new HttpHost("127.0.0.1", port);
        return this;
    }

    public HttpSite build() {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(this.timeout * 1000)
                .setConnectionRequestTimeout(this.timeout * 1000)
                .setSocketTimeout(this.timeout * 1000);

        if (proxy != null) {
            requestConfigBuilder.setProxy(this.proxy);
        }

        RequestConfig requestConfig = requestConfigBuilder.build();

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setSSLSocketFactory(getSslSocketFactory());
        if (chromeMode) {
            httpClientBuilder.setUserAgent(USER_AGENT);
        }
        CloseableHttpClient httpClient = httpClientBuilder.build();
        return new HttpSite(httpClient);
    }

    @SneakyThrows
    private SSLConnectionSocketFactory getSslSocketFactory() {
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        sslContextBuilder.loadTrustMaterial(TrustAllStrategy.INSTANCE);
        SSLContext sslContext = sslContextBuilder.build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        return sslSocketFactory;
    }
}
