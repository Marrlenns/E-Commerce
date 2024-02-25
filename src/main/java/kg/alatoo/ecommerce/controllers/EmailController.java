package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.user.CodeRequest;
import kg.alatoo.ecommerce.dto.user.EmailRequest;
import kg.alatoo.ecommerce.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping()
    public String code(@RequestHeader("Authorization") String token, @RequestBody EmailRequest request){
        emailService.send_code(token, request);
        return "We have sent a code to your email!";
    }

    @PostMapping("/verify")
    public String verify(@RequestHeader("Authorization") String token, @RequestBody CodeRequest request){
        emailService.verify(token, request);
        return "Your email is linked successfully!";
    }

}
