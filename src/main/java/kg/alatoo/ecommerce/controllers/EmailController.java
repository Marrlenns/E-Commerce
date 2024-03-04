package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.user.CodeRequest;
import kg.alatoo.ecommerce.dto.user.EmailRequest;
import kg.alatoo.ecommerce.dto.user.RecoveryRequest;
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

    @PostMapping("/recovery")
    public String recovery(@RequestBody EmailRequest request){
        emailService.recovery(request.getEmail());
        return "We sent a link for recovery your password to your email!";
    }

    @PostMapping("/recovery-password")
    public String recoveryPassword(@RequestParam String code, @RequestBody RecoveryRequest request){
        emailService.recoveryPassword(code, request);
        return "Password updated successfully!";
    }

}
