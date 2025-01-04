package io.codegitters.sb_http_client_demo_a.api;

import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.jayway.jsonpath.JsonPath.read;

@Slf4j
@RestController
@RequestMapping("/jsonplaceholder")
@RequiredArgsConstructor
public class JsonPlaceHolderController {
    
    private static final String JSONPLACEHOLDER_URI_BASE = "https://jsonplaceholder.typicode.com";
    
    @GetMapping("/users")
    public Mono<ResponseEntity<byte[]>> getAll(@Schema(hidden = true) ProxyExchange<byte[]> proxy) throws Exception {
        log.debug("proxy: {}", proxy);
        return proxy.uri(JSONPLACEHOLDER_URI_BASE.concat("/users"))
                .get();
    }
    
    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<byte[]>> getById(@Schema(hidden = true) ProxyExchange<byte[]> proxy,
                                                @PathVariable String id) throws Exception {
        log.debug("proxy: {}", proxy);
        log.debug("path: {}", proxy.path());
        return proxy.uri(JSONPLACEHOLDER_URI_BASE.concat("/users/:id")
                        .replace(":id", id))
                .get();
    }

    @GetMapping("/users/{id}/address")
    public Mono<ResponseEntity<?>> getAddressByUserId(@Schema(hidden = true) ProxyExchange<byte[]> proxy, 
                                                      @PathVariable String id) {
        log.debug("path: {}", proxy.path());
        return proxy
                .uri(JSONPLACEHOLDER_URI_BASE.concat("/users/:id")
                        .replace(":id", id))
                .get()
                .doOnNext(response -> log.debug("headers: {}", response.getHeaders()))
                .doOnNext(response -> log.debug("content: {}", new String(response.getBody())))
                .map(response -> ResponseEntity
                        .status(response.getStatusCode())
                        .headers(response.getHeaders())
                        .body(read(new String(response.getBody()), "$.address")));
    }

    @GetMapping("/comments")
    public Single<ResponseEntity<?>> getAllComments(@Schema(hidden = true) ProxyExchange<byte[]> proxy) {
        return proxy
                .uri(JSONPLACEHOLDER_URI_BASE.concat("/comments"))
                .get()
                .as(Single::fromPublisher);
    }
}
