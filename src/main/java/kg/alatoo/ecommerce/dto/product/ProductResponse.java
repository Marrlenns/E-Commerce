package kg.alatoo.ecommerce.dto.product;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kg.alatoo.ecommerce.enums.Color;
import kg.alatoo.ecommerce.enums.Size;
import kg.alatoo.ecommerce.enums.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String imageName;
    private String title;
    private Integer price;
    private String category;
}