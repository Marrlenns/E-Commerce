package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.user.CodeRequest;
import kg.alatoo.ecommerce.dto.user.EmailRequest;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailSender mailSender;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Override
    public void send_code(String token, EmailRequest request) {
        String code = "";
        Random random = new Random();
        for(int k = 0; k < 6; k++){
            if(random.nextInt(2) == 0)
                code += (char) (random.nextInt(26) + 65);
            else
                code += (char) (random.nextInt(10) + 48);

        }

        User user = authService.getUserFromToken(token);
        user.setVerifyCode(code);
        user.setEmail(request.getEmail());
        userRepository.save(user);

        String email = request.getEmail();
        if(email == null)
            throw new BadRequestException("Please, write your email!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("marlenormonbaev@gmail.com");
        message.setTo(email);
        message.setText("This is code for verifying your account: " + code + "\n\nDon't share it!!!");
        message.setSubject("E-Commerce. Account verifying");
        mailSender.send(message);
    }

    @Override
    public void verify(String token, CodeRequest request) {
        User user = authService.getUserFromToken(token);
        if(Objects.equals(user.getVerifyCode(), request.getCode())){
            user.setVerified(true);
            userRepository.save(user);
        } else
            throw new BadRequestException("Code is wrong!");

    }
}
