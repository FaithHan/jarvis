package org.jarvis.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    int httpStatusCode;

    Headers header;

    T data;

    public String getContentType() {
        return header.getHeader("Content-Type");
    }
}
