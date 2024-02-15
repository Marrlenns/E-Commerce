package kg.alatoo.ecommerce.services.impl;

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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLoginRequest.getUsername(),authLoginRequest.getPassword()));
        }catch (org.springframework.security.authentication.BadCredentialsException e){
            throw new BadCredentialsException("Credentials are incorrect!");
        }

        return convertToResponse(user.get());
    }

    private AuthLoginResponse convertToResponse(User user) {
        AuthLoginResponse response = new AuthLoginResponse();
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        Map<String, Object> extraClaims = new HashMap<>();

        String token = jwtService.generateToken(extraClaims, user);
        response.setToken(token);

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
}
