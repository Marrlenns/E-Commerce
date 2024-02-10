package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.UserRegisterRequest;
import kg.alatoo.ecommerce.dto.user.PasswordRequest;
import kg.alatoo.ecommerce.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PutMapping("/update")
    public String update(@RequestBody UserRegisterRequest request, @RequestHeader("Authorization") String token){
        userService.update(token, request);
        return "Data updated successfully!";
    }

    @PutMapping("/update/password")
    public String update(@RequestHeader("Authorization") String token, @RequestBody PasswordRequest request){
        userService.updatePassword(token, request);
        return "Password changed successfully!";
    }

}
