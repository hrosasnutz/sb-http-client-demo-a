package io.codegitters.sb_http_client_demo_a.io.jsonplaceholder.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostResponse {
    private Long id;
    private String title;
    private String body;
    private Long userId;
}
