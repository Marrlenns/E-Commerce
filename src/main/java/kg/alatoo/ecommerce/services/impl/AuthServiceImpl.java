package kg.alatoo.ecommerce.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import  kg.alatoo.ecommerce.config.JwtService;
import  kg.alatoo.ecommerce.dto.AuthLoginRequest;
import  kg.alatoo.ecommerce.dto.AuthLoginResponse;
import  kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.entities.Cart;
import kg.alatoo.ecommerce.entities.Token;
import  kg.alatoo.ecommerce.entities.User;
import  kg.alatoo.ecommerce.enums.Role;
import kg.alatoo.ecommerce.enums.TokenType;
import  kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.repositories.CartRepository;
import kg.alatoo.ecommerce.repositories.TokenRepository;
import  kg.alatoo.ecommerce.repositories.UserRepository;
import  kg.alatoo.ecommerce.services.AuthService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final TokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new BadCredentialsException("User with username: " + request.getUsername() + " is already exist!");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.CLIENT);
        user.setVerified(false);
        User user1 = userRepository.saveAndFlush(user);
        Cart cart = new Cart();
        cart.setUser(user);
        Cart cart1 = cartRepository.saveAndFlush(cart);
        user1.setCart(cart1);
        userRepository.save(user1);

    }

    @Override
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {

        Optional<User> user = userRepository.findByUsername(authLoginRequest.getUsername());
        if (user.isEmpty())
            throw new BadRequestException("Invalid username or password!");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authLoginRequest.getUsername(),authLoginRequest.getPassword()
                    ));
        }catch (org.springframework.security.authentication.BadCredentialsException e){
            throw new BadCredentialsException("Credentials are incorrect!");
        }

        return convertToResponse(user.get());
    }

    private AuthLoginResponse convertToResponse(User user) {
        AuthLoginResponse response = new AuthLoginResponse();
        Map<String, Object> extraClaims = new HashMap<>();

        String token = jwtService.generateToken(extraClaims, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, token);
        response.setAccessToken(token);
        response.setRefreshToken(refreshToken);

        return response;
    }

    @Override
    public User getUserFromToken(String token){

        String[] chunks = token.substring(7).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        if (chunks.length != 3)
            throw new BadCredentialsException("Wrong token!");
        JSONParser jsonParser = new JSONParser();
        JSONObject object = null;
        try {
            byte[] decodedBytes = decoder.decode(chunks[1]);
            object = (JSONObject) jsonParser.parse(decodedBytes);
        } catch (ParseException e) {
            throw new BadCredentialsException("Wrong token!!");
        }
        return userRepository.findByUsername(String.valueOf(object.get("sub"))).orElseThrow(() -> new BadCredentialsException("Wrong token!!!"));
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String username;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if(username != null){
            var user = this.userRepository.findByUsername(username).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                AuthLoginResponse authResponse = new AuthLoginResponse();
                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
