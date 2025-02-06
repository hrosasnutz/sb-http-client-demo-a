package io.codegitters.sb_http_client_demo_a.config;

import io.codegitters.sb_http_client_demo_a.client.JsonPlaceHolderApiClient;
import io.codegitters.sb_http_client_demo_a.config.filter.DefaultWebClientExchangeFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApiClientConfig {

    @SneakyThrows
    private SslContext noOpSslContext() {
        return SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
    }
    
    @Bean
    WebClient.Builder apiWebClientBuilder() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                        .doOnConnected(cnn -> cnn
                                .addHandlerFirst(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)))
                        .responseTimeout(Duration.ofSeconds(5))
                        .secure(ctx -> ctx.sslContext(noOpSslContext()))))
                .filter(new DefaultWebClientExchangeFilter());
//                .exchangeStrategies();// SET CUSTOM OBJECT MAPPER
    }
    
    @Bean
    WebClientAdapter jsonPlaceHolderWebClientAdapter() {
        return WebClientAdapter.create(apiWebClientBuilder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build());
    }
    
    @Bean
    JsonPlaceHolderApiClient jsonPlaceHolderApiClient() {
        return HttpServiceProxyFactory
                .builderFor(jsonPlaceHolderWebClientAdapter())
                .build()
                .createClient(JsonPlaceHolderApiClient.class);
    }
    
    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> defaultConfig() {
        return factory -> factory.configureDefault(id -> 
                new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofSeconds(5)).build())
                        .build());
    }
            
}
