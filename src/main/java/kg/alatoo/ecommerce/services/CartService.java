package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.cart.AddToCartRequest;

public interface CartService {
    void add(AddToCartRequest request, String token);

    void update(AddToCartRequest request, String token);

    void delete(AddToCartRequest request, String token);
}
