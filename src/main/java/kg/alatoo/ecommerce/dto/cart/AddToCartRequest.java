package kg.alatoo.ecommerce.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private Long productId;
    private Integer quantity;
}
