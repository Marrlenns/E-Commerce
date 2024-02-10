package kg.alatoo.ecommerce.mappers.impl;

import kg.alatoo.ecommerce.dto.product.ProductComparisonResponse;
import kg.alatoo.ecommerce.dto.product.ProductDetailResponse;
import kg.alatoo.ecommerce.dto.product.ProductResponse;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.Review;
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
        double rating = 0;
        List<String> reviews = new ArrayList<>();
        for(Review review: product.getReviews()){
            rating += review.getStars();
            reviews.add(review.getText());
        }
        productResponse.setReviews(reviews);
        if(product.getReviews().isEmpty())
            productResponse.setRating("0.0");
        else
            productResponse.setRating(String.format("%.1f", rating / product.getReviews().size()));
        return productResponse;
    }

    @Override
    public ProductComparisonResponse toCompareDtos(Product product1, Product product2) {
        ProductComparisonResponse response = new ProductComparisonResponse();

        response.setId1(product1.getId());
        response.setId2(product2.getId());

        response.setCategory1(product1.getCategory().getTitle());
        response.setCategory2(product2.getCategory().getTitle());

        response.setColors1(product1.getColors());
        response.setColors2(product2.getColors());

        response.setPrice1(product1.getPrice());
        response.setPrice2(product2.getPrice());

        response.setTags1(product1.getTags());
        response.setTags2(product2.getTags());

        response.setTitle1(product1.getTitle());
        response.setTitle2(product2.getTitle());

        response.setSku1(product1.getSku());
        response.setSku2(product2.getSku());

        response.setSizes1(product1.getSizes());
        response.setSizes2(product2.getSizes());

        response.setRevSize1(product1.getReviews().size());
        response.setRevSize2(product2.getReviews().size());

        if(product1.getReviews().isEmpty())
            response.setRating1("0.0");
        else{
            double rating = 0;
            for(Review review: product1.getReviews())
                rating += review.getStars();
            response.setRating1(String.format("%.1f", rating / product1.getReviews().size()));
        }

        if(product2.getReviews().isEmpty())
            response.setRating2("0.0");
        else{
            double rating = 0;
            for(Review review: product2.getReviews())
                rating += review.getStars();
            response.setRating2(String.format("%.1f", rating / product2.getReviews().size()));
        }

        return response;
    }
}
