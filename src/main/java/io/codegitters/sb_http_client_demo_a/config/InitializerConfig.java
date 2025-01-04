package io.codegitters.sb_http_client_demo_a.config;

import io.codegitters.sb_http_client_demo_a.client.JsonPlaceHolderApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitializerConfig implements ApplicationRunner {
    
    private final JsonPlaceHolderApiClient jsonPlaceHolderApiClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jsonPlaceHolderApiClient.getAllUsers()
                .next()
                .doOnNext(u -> log.debug("First User by Reactor: {}", u))
                .doOnError(e -> log.error("Error on get All Users", e))
                .subscribe();
        
        jsonPlaceHolderApiClient.getAllComments()
                .firstElement()
                .doOnSuccess(c -> log.debug("First Comment by RxJava: {}", c))
                .doOnError(e -> log.error("Error on get All Comments", e))
                .subscribe();
        
        jsonPlaceHolderApiClient.getCommentById(-1L)
                .doOnError(e -> log.error("Error on get Comment by Id", e))
                .subscribe();
    }
}
