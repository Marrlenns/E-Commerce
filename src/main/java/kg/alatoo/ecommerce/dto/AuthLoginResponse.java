package kg.alatoo.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponse {
    private String accessToken;
    private String refreshToken;
}
