package kg.alatoo.ecommerce.dto.order;

import jakarta.persistence.ManyToOne;
import kg.alatoo.ecommerce.dto.image.ImageResponse;
import kg.alatoo.ecommerce.entities.Image;
import kg.alatoo.ecommerce.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDetailResponse {

    private Long id;
    private LocalDateTime createDate;
    private String title;
    private ImageResponse image;
    private Integer price;
    private Integer total;
    private Integer quantity;
    private String sku;
}
