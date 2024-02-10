package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.dto.user.PasswordRequest;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void update(String token, UserRegisterRequest request) {
        Optional<User> user = Optional.ofNullable(authService.getUserFromToken(token));
        if(request.getEmail() != null)
            user.get().setEmail(request.getEmail());

        Optional<User> user1 = userRepository.findByUsername(request.getUsername());
        if(user1.isEmpty() || user1.get() == user.get())
            user.get().setUsername(request.getUsername());
        else
            throw new BadRequestException("This username already in use!");

        userRepository.save(user.get());
    }

    @Override
    public void updatePassword(String token, PasswordRequest request) {
        User user = authService.getUserFromToken(token);
        String password = user.getPassword();
        String oldPassword = request.getOldPassword();
        String newPassword1 = request.getNewPassword1();
        String newPassword2 = request.getNewPassword2();
        if(!Objects.equals(newPassword2, newPassword1))
            throw new BadRequestException("Passwords aren't match!");
        if(!encoder.matches(oldPassword, password))
            throw new BadRequestException("Wrong old password!");
        user.setPassword(encoder.encode(newPassword1));
        userRepository.save(user);
    }

}
