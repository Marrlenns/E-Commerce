package kg.alatoo.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponse {
    private Long id;
    private String username;
    private String email;
    private String token;
}
