package org.jarvis.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Request<T> {

    private HttpMethod httpMethod;

    private String uri;

    private QueryParams queryParams;

    private Headers headers;

    /**
     *  String,DataObj,MultipartEntity,FormEntity
     */
    private T requestBody;


    public static RequestBuilder get(String uri) {
        return new RequestBuilder(uri, HttpMethod.GET);
    }

    public static RequestBuilder post(String uri) {
        return new RequestBuilder(uri, HttpMethod.POST);
    }

    public static RequestBuilder head(String uri) {
        return new RequestBuilder(uri, HttpMethod.HEAD);
    }

    public static RequestBuilder put(String uri) {
        return new RequestBuilder(uri, HttpMethod.PUT);
    }

    public static RequestBuilder delete(String uri) {
        return new RequestBuilder(uri, HttpMethod.DELETE);
    }

    @NoArgsConstructor
    static class RequestBuilder {

        private String uri;

        private HttpMethod httpMethod;

        private Headers headers;

        private QueryParams queryParams;

        public RequestBuilder(String uri, HttpMethod httpMethod) {
            this.uri = uri;
            this.httpMethod = httpMethod;
        }

        public RequestBuilder header(String headerName, String headerValue) {
            if (this.headers == null) {
                this.headers = new Headers();
            }
            this.headers.addHeader(headerName, headerValue);
            return this;
        }

        public RequestBuilder headers(Headers headers) {
            if (headers == null) {
                return this;
            }
            if (this.headers == null) {
                this.headers = new Headers();
            }
            this.headers.addHeaders(headers);
            return this;
        }

        public RequestBuilder queryParam(String paramName, String paramValue) {
            if (this.queryParams == null) {
                this.queryParams = new QueryParams();
            }
            this.queryParams.addParam(paramName, paramValue);
            return this;
        }

        public RequestBuilder queryParams(QueryParams queryParams) {
            if (this.queryParams == null) {
                this.queryParams = new QueryParams();
            }
            this.queryParams.addParams(queryParams);
            return this;
        }

        public Request<Void> build() {
            Request<Void> request = new Request<>();
            request.uri = uri;
            request.httpMethod = httpMethod;
            request.queryParams = queryParams;
            request.headers = headers;
            return request;
        }

        public <E> Request<E> build(E body) {
            Request<E> request = new Request<>();
            request.setUri(this.uri);
            request.setHttpMethod(this.httpMethod);
            request.setQueryParams(this.queryParams);
            request.setHeaders(this.headers);
            request.setRequestBody(body);
            return request;
        }

    }

}
