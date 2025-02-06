package io.codegitters.sb_http_client_demo_a.config.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class LoggingClientHttpRequestDecorator extends ClientHttpRequestDecorator {

    public LoggingClientHttpRequestDecorator(ClientHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return DataBufferUtils.join(body)
                .doOnNext(buffer -> logRequest(this.getDelegate(), buffer))
                .flatMap(db -> super.writeWith(Mono.just(db)));
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return Mono.from(body)
                .flatMap(DataBufferUtils::join)
                .doOnNext(buffer -> logRequest(this.getDelegate(), buffer))
                .flatMap(buffer -> super.writeAndFlushWith(Mono.just(Mono.just(buffer))));
    }

    private void logRequest(ClientHttpRequest request, DataBuffer buffer) {
        var requestLog = RequestLog.builder()
                .uri(request.getURI())
                .method(request.getMethod())
                .headers(request.getHeaders())
                .attributes(request.getAttributes())
                .cookies(request.getCookies())
                .body(buffer.toString(StandardCharsets.UTF_8))
                .build();
        log.debug("request log: {}", requestLog);
    }
    
    @Data
    @With
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RequestLog {
        private URI uri;
        private HttpMethod method;
        private HttpHeaders headers;
        private Map<String, Object> attributes;
        private MultiValueMap<String, HttpCookie> cookies;
        private String body;
    }
}
