package kg.alatoo.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private String text;
    private Integer stars;
}
