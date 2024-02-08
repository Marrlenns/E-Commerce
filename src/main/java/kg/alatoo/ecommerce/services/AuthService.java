package kg.alatoo.ecommerce.services;

import  kg.alatoo.ecommerce.dto.AuthLoginRequest;
import  kg.alatoo.ecommerce.dto.AuthLoginResponse;
import  kg.alatoo.ecommerce.dto.UserRegisterRequest;
import  kg.alatoo.ecommerce.entities.User;

public interface AuthService {
    void register(UserRegisterRequest userRegisterRequest);

    AuthLoginResponse login(AuthLoginRequest authLoginRequest);

    public User getUserFromToken(String token);
}
