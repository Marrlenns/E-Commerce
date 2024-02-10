package kg.alatoo.ecommerce.mappers;

import kg.alatoo.ecommerce.dto.product.ProductComparisonResponse;
import kg.alatoo.ecommerce.dto.product.ProductDetailResponse;
import kg.alatoo.ecommerce.dto.product.ProductResponse;
import kg.alatoo.ecommerce.entities.Product;

import java.util.List;

public interface ProductMapper {
    List<ProductResponse> toDtos(List<Product> all);

    ProductDetailResponse toDetailDto(Product product);

    ProductComparisonResponse toCompareDtos(Product product, Product product1);
}
