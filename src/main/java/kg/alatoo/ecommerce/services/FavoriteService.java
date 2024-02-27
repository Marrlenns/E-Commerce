package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.product.ProductResponse;

import java.util.List;

public interface FavoriteService {
    void add(String token, Long id);

    List<ProductResponse> all(String token);

    void delete(String token, Long id);
}
