package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.product.CategoryRequest;
import kg.alatoo.ecommerce.dto.product.ProductRequest;
import kg.alatoo.ecommerce.dto.product.ProductResponse;

public interface ProductService {
    void addCategory(CategoryRequest request);

    void addProduct(ProductRequest request, String token);

    void updateById(Long id, ProductRequest productRequest, String token);

    ProductResponse showById(Long id);

    void deleteById(Long id, String token);
}