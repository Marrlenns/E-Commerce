package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.user.CodeRequest;
import kg.alatoo.ecommerce.dto.user.EmailRequest;

public interface EmailService {
    void send_code(String token, EmailRequest request);

    void verify(String token, CodeRequest request);
}
