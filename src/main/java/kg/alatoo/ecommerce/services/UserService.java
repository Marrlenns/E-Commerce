package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.dto.user.PasswordRequest;
import kg.alatoo.ecommerce.dto.user.ProfileResponse;

public interface UserService {
    void update(String token, ProfileResponse request);

    void updatePassword(String token, PasswordRequest request);

    ProfileResponse profile(String token);
}
