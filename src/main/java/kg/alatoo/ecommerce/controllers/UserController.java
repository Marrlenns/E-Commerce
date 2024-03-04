package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.dto.user.PasswordRequest;
import kg.alatoo.ecommerce.dto.user.ProfileResponse;
import kg.alatoo.ecommerce.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ProfileResponse profile(@RequestHeader("Authorization") String token){
        return userService.profile(token);
    }

    @PutMapping("/update")
    public String update(@RequestBody ProfileResponse request, @RequestHeader("Authorization") String token){
        userService.update(token, request);
        return "Data updated successfully!";
    }

    @PutMapping("/update/password")
    public String update(@RequestHeader("Authorization") String token, @RequestBody PasswordRequest request){
        userService.updatePassword(token, request);
        return "Password changed successfully!";
    }

}
