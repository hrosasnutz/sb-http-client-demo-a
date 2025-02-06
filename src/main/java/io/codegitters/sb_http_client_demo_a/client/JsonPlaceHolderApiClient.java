package io.codegitters.sb_http_client_demo_a.client;

import io.codegitters.sb_http_client_demo_a.io.jsonplaceholder.request.CreatePostRequest;
import io.codegitters.sb_http_client_demo_a.io.jsonplaceholder.response.CommentResponse;
import io.codegitters.sb_http_client_demo_a.io.jsonplaceholder.response.CreatePostResponse;
import io.codegitters.sb_http_client_demo_a.io.jsonplaceholder.response.UserResponse;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JsonPlaceHolderApiClient {
    
    @GetExchange(url = "/users", accept = MediaType.APPLICATION_JSON_VALUE)
    Flux<UserResponse> getAllUsers();

    @GetExchange(url = "/users/{id}", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<UserResponse> getUserById(@PathVariable Long id);

    @GetExchange(url = "/comments", accept = MediaType.APPLICATION_JSON_VALUE)
    Flowable<CommentResponse> getAllComments();

    @GetExchange(url = "/comments/{id}", accept = MediaType.APPLICATION_JSON_VALUE)
    Maybe<CommentResponse> getCommentById(@PathVariable Long id);
    
    @PostExchange(url = "/posts", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
    Single<ResponseEntity<CreatePostResponse>> createPost(@RequestBody CreatePostRequest createPostRequest);
}
