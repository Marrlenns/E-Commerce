package kg.alatoo.ecommerce.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import  kg.alatoo.ecommerce.dto.AuthLoginRequest;
import  kg.alatoo.ecommerce.dto.AuthLoginResponse;
import  kg.alatoo.ecommerce.dto.UserRegisterRequest;
import  kg.alatoo.ecommerce.entities.User;

import java.io.IOException;

public interface AuthService {
    void register(UserRegisterRequest userRegisterRequest);

    AuthLoginResponse login(AuthLoginRequest authLoginRequest);

    public User getUserFromToken(String token);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
