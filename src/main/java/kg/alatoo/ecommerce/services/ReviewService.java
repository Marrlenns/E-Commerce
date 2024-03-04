package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.product.ReviewRequest;

public interface ReviewService {
    void addReview(Long id, String token, ReviewRequest request);

    void updateReview(Long id, String token, ReviewRequest request, Long idd);

    void deleteReview(String token, Long id, Long idd);
}
