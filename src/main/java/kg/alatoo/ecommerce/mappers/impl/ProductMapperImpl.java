package kg.alatoo.ecommerce.mappers.impl;

import kg.alatoo.ecommerce.dto.product.ProductDetailResponse;
import kg.alatoo.ecommerce.dto.product.ProductResponse;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.mappers.ProductMapper;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductMapperImpl implements ProductMapper {
    
    public ProductResponse toDto(Product product){
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory().getTitle());
        return response;
    }

    @Override
    public List<ProductResponse> toDtos(List<Product> all) {
        List<ProductResponse> products = new ArrayList<>();
        for(Product product: all){
            products.add(toDto(product));
        }
        return products;
    }

    @Override
    public ProductDetailResponse toDetailDto(Product product) {
        ProductDetailResponse productResponse = new ProductDetailResponse();
        productResponse.setId(product.getId());
        productResponse.setTitle(product.getTitle());
        productResponse.setPrice(product.getPrice());
        productResponse.setDescription(product.getDescription());
        productResponse.setColors(product.getColors());
        productResponse.setTags(product.getTags());
        productResponse.setSizes(product.getSizes());
        productResponse.setCategory(product.getCategory().getTitle());
        productResponse.setSku(product.getSku());
        return productResponse;
    }
}
