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
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private AddressResponse address;
    private String phone;
    private String website;
    private CompanyResponse company;

    @Data
    @With
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressResponse {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private GeoResponse geo;
    }

    @Data
    @With
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeoResponse {
        private String lat;
        private String lng;
    }

    @Data
    @With
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyResponse {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}