package kg.alatoo.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequest {
    private String username;
    private String password;
}
