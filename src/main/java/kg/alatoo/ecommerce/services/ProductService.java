package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.product.CategoryRequest;
import kg.alatoo.ecommerce.dto.product.ProductRequest;

public interface ProductService {
    void addCategory(CategoryRequest request);

    void addProduct(ProductRequest request);

    void updateById(Long id, ProductRequest productRequest);
}
