package io.codegitters.sb_http_client_demo_a.config;

import io.codegitters.sb_http_client_demo_a.client.JsonPlaceHolderApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ApiClientConfig {
    
    @Bean
    WebClientAdapter jsonPlaceHolderWebClientAdapter() {
        return WebClientAdapter.create(WebClient
                .create("https://jsonplaceholder.typicode.com/"));
    }
    
    @Bean
    JsonPlaceHolderApiClient jsonPlaceHolderApiClient() {
        return HttpServiceProxyFactory
                .builderFor(jsonPlaceHolderWebClientAdapter())
                .build()
                .createClient(JsonPlaceHolderApiClient.class);
    }
}
