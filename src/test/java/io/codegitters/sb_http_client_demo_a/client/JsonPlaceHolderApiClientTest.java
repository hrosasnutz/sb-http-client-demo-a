package io.codegitters.sb_http_client_demo_a.client;

import io.codegitters.sb_http_client_demo_a.SbHttpClientDemoAApplicationTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

class JsonPlaceHolderApiClientTest extends SbHttpClientDemoAApplicationTests {
    
    @Autowired
    private JsonPlaceHolderApiClient client;
    
    @BeforeEach
    void setUp() {
    }

    @Test
    void getAll() {
        client.getAllUsers()
                .doOnError(Throwable::printStackTrace)
                .map(u -> u)
                .log()
                .doOnNext(System.out::println)
                .subscribe();
    }
    
    @Test
    void getById() {
        client.getUserById(1L)
                .log()
                .doOnNext(u -> System.out.println(u))
                .doOnError(e -> System.err.println(e))
                .subscribe()
                .dispose();
        StepVerifier.create(client.getUserById(1L))
                .expectNextCount(1L)
                .verifyComplete();
    }
}