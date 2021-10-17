package org.jarvis.spring.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 * https://www.baeldung.com/exception-handling-for-rest-with-spring
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .code(1)
                .message(exception.getMessage())
                .stackTrace(stringWriter.toString())
                .dateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

}
