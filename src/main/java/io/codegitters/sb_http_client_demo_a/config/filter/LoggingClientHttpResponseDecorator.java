package io.codegitters.sb_http_client_demo_a.config.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingClientHttpResponseDecorator extends ClientHttpResponseDecorator {
    
    public LoggingClientHttpResponseDecorator(ClientHttpResponse delegate) {
        super(delegate);
    }
    
    public void logResponse() {
        this.getBody()
                .map(buffer -> ResponseLog.builder()
                        .id(this.getId())
                        .statusCode(this.getStatusCode())
                        .headers(this.getHeaders())
                        .cookies(this.getCookies())
                        .body(buffer.toString(StandardCharsets.UTF_8))
                        .build())
                .doOnNext(rl -> log.debug("Response log: {}", rl))
                .subscribe();
    }
    
    @Data
    @With
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResponseLog {
        private String id;
        private HttpStatusCode statusCode;
        private HttpHeaders headers;
        private MultiValueMap<String, ResponseCookie> cookies;
        private String body;
    }
}
