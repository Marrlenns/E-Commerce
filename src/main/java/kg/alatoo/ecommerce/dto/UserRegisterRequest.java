package kg.alatoo.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {
    private String fullName;
    private String nickname;
    private String password;
    private Integer age;
    private String role;
}
