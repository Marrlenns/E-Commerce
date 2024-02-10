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
public class ProductDetailResponse {
    private Long id;
    private String title;
    private Double rating;
    private Integer price;
    private String description;
    private List<Color> colors;
    private List<Tag> tags;
    private List<Size> sizes;
    private String category;
    private String sku;
    private List<String> reviews;
}