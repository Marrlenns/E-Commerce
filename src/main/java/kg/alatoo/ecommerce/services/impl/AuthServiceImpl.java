package kg.alatoo.ecommerce.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import  kg.alatoo.ecommerce.config.JwtService;
import  kg.alatoo.ecommerce.dto.AuthLoginRequest;
import  kg.alatoo.ecommerce.dto.AuthLoginResponse;
import  kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.entities.Cart;
import  kg.alatoo.ecommerce.entities.User;
import  kg.alatoo.ecommerce.enums.Role;
import  kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.repositories.CartRepository;
import  kg.alatoo.ecommerce.repositories.UserRepository;
import  kg.alatoo.ecommerce.services.AuthService;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;

    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new BadCredentialsException("User with username: " + request.getUsername() + " is already exist!");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.CLIENT);
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
            throw new IllegalArgumentException("Invalid username or password!");
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
            var userDetails = this.userRepository.findByUsername(username).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, userDetails)){
                var accessToken = jwtService.generateToken(userDetails);
                AuthLoginResponse authResponse = new AuthLoginResponse();
                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
