package kg.alatoo.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponse {
    private Long id;
    private String nickname;
    private String fullName;
    private String token;
}
