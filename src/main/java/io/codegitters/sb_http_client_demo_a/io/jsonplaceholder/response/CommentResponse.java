package io.codegitters.sb_http_client_demo_a.io.jsonplaceholder.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long postId;
    private Long id;
    private String name;
    private String email;
    private String body;
}
