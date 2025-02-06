package io.codegitters.sb_http_client_demo_a.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class DefaultWebClientExchangeFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        var copy = ClientRequest.from(request)
                .body((msg, ctx) -> request.body()
                        .insert(new LoggingClientHttpRequestDecorator(msg), ctx))
                .build();
        return Mono.defer(() -> next.exchange(copy))
                .doOnNext(resp -> new LoggingClientHttpResponseDecorator(resp).logResponse());
    }
}
