package kg.alatoo.ecommerce.mappers;

import kg.alatoo.ecommerce.dto.cart.CartResponse;
import kg.alatoo.ecommerce.entities.Cart;

public interface CartMapper {
    CartResponse toDto(Cart cart);
}
