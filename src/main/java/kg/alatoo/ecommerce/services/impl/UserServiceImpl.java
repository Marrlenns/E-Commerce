package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.user.PasswordRequest;
import kg.alatoo.ecommerce.dto.user.ProfileResponse;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.mappers.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public void update(String token, ProfileResponse request) {
        User user = authService.getUserFromToken(token);

        Optional<User> user1 = userRepository.findByUsername(request.getUsername());
        if(request.getUsername() != null){
            if(user1.isEmpty() || user1.get() == user)
                user.setUsername(request.getUsername());
            else
                throw new BadRequestException("This username already in use!");
        }


        if(request.getEmail() != null)
            user.setEmail(request.getEmail());
        if(request.getFirstName() != null)
            user.setFirstName(request.getFirstName());
        if(request.getLastName() != null)
            user.setLastName(request.getLastName());
        if(request.getCompanyName() != null)
            user.setCompanyName(request.getCompanyName());
        if(request.getCountry() != null)
            user.setCountry(request.getCountry());
        if(request.getStreetAddress() != null)
            user.setStreetAddress(request.getStreetAddress());
        if(request.getCity() != null)
            user.setCity(request.getCity());
        if(request.getProvince() != null)
            user.setProvince(request.getProvince());
        if(request.getZipCode() != null)
            user.setZipCode(request.getZipCode());
        if(request.getPhone() != null)
            user.setPhone(request.getPhone());
        if(request.getAdditionalInfo() != null)
            user.setAdditionalInfo(request.getAdditionalInfo());


        userRepository.save(user);
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

    @Override
    public ProfileResponse profile(String token) {
        User user = authService.getUserFromToken(token);
        return userMapper.toDto(user);
    }

}
