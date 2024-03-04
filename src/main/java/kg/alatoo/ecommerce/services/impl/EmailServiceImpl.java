package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.user.CodeRequest;
import kg.alatoo.ecommerce.dto.user.EmailRequest;
import kg.alatoo.ecommerce.dto.user.RecoveryRequest;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailSender mailSender;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

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

    @Override
    public void recovery(String email) {
        UUID uuid = UUID.randomUUID();
        String code = uuid.toString();
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty())
            throw new NotFoundException("Account with this email doesn't exist!", HttpStatus.NOT_FOUND);
        user.get().setUuid(code);
        userRepository.save(user.get());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("marlenormonbaev@gmail.com");
        message.setTo(email);
        message.setText("This is link for recovery your password: http://localhost:8080/api/v1/email/recovery-password?code=" + code + "\n\nDon't share it!!!");
        message.setSubject("E-Commerce. Account verifying");
        mailSender.send(message);
    }

    @Override
    public void recoveryPassword(String code, RecoveryRequest request) {
        Optional<User> user = userRepository.findByUuid(code);
        String pass1 = request.getNewPassword1();
        String pass2 = request.getNewPassword2();
        if(!Objects.equals(pass1, pass2))
            throw new BadRequestException("Passwords don't match!");
        user.get().setPassword(encoder.encode(pass1));
        userRepository.save(user.get());
    }
}
