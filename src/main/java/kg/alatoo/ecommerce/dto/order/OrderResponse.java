package kg.alatoo.ecommerce.dto.order;

import jakarta.persistence.ManyToOne;
import kg.alatoo.ecommerce.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class OrderResponse {
    private Long id;
    private LocalDateTime createDate;
    private String title;
    private String imageName;
    private Integer price;
}
