package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.dto.user.PasswordRequest;

public interface UserService {
    void update(String token, UserRegisterRequest request);

    void updatePassword(String token, PasswordRequest request);
}
