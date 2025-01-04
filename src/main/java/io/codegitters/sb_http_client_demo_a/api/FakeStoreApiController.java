package io.codegitters.sb_http_client_demo_a.api;

import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fakestoreapi")
public class FakeStoreApiController {
    
    private static final String FAKESTOREAPI_URI = "https://fakestoreapi.com";
    
    @GetMapping("/products")
    public Single<ResponseEntity<byte[]>> getAllProducts(ProxyExchange<byte[]> proxy) {
        return proxy.uri(FAKESTOREAPI_URI.concat("/products"))
                .get()
                .doOnNext(response -> log.debug("content: {}", new String(response.getBody())))
                .as(Single::fromPublisher);
    }
}
