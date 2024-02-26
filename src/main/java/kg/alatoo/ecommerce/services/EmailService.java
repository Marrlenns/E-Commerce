package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.user.CodeRequest;
import kg.alatoo.ecommerce.dto.user.EmailRequest;
import kg.alatoo.ecommerce.dto.user.RecoveryRequest;

public interface EmailService {
    void send_code(String token, EmailRequest request);

    void verify(String token, CodeRequest request);

    void recovery(String email);

    void recoveryPassword(String code, RecoveryRequest request);
}
