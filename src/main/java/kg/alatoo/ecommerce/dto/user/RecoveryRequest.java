package kg.alatoo.ecommerce.dto.user;

import lombok.Data;

@Data
public class RecoveryRequest {
    private String newPassword1;
    private String newPassword2;
}
