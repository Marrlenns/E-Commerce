package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.product.ReviewRequest;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.Review;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import kg.alatoo.ecommerce.repositories.ReviewRepository;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final AuthService authService;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public void addReview(Long id, String token, ReviewRequest request) {
        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty())
            throw new BadRequestException("Product with id: " + id + " - doesn't exist!");

        if (request.getStars() > 5 || request.getStars() < 0)
            throw new BadRequestException("U can rate this product only in range 0-5");

        User user = authService.getUserFromToken(token);

        List<Review> reviewList = reviewRepository.findByUserAndProduct(user, product.get());
        boolean flag = false;
        for(Review review: reviewList){
            if(review.getStars() != null){
                flag = true;
                break;
            }
        }

        Review review = new Review();
        review.setText(request.getText());
        if(!flag)
            review.setStars(request.getStars());
        review.setProduct(product.get());
        review.setUser(user);
        Review review1 = reviewRepository.saveAndFlush(review);

        List<Review> reviews = new ArrayList<>();
        if(product.get().getReviews() != null)
            reviews = product.get().getReviews();
        reviews.add(review1);
        product.get().setReviews(reviews);
        productRepository.save(product.get());

        reviews = new ArrayList<>();
        if(user.getReviews() != null)
            reviews = user.getReviews();
        reviews.add(review1);
        user.setReviews(reviews);
        userRepository.save(user);
    }

    @Override
    public void updateReview(Long id, String token, ReviewRequest request, Long idd){
        Optional<Review> review = reviewRepository.findById(idd);
        if(review.isEmpty())
            throw new BadRequestException("This review doesn't exist!");
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty())
            throw new BadRequestException("This product doesn't exist!");
        User user = authService.getUserFromToken(token);
        if(review.get().getUser() != user)
            throw new BadCredentialsException("You're not allowed to update this review!");
        if(review.get().getProduct() != product.get())
            throw new BadRequestException("This product doesn't have this review!");
        if (request.getStars() > 5 || request.getStars() < 0)
            throw new BadRequestException("U can rate this product only in range 0-5");

        List<Review> reviews = reviewRepository.findByUserAndProduct(user, product.get());
        for(Review review1: reviews)
            review1.setStars(null);

        review.get().setText(request.getText());
        review.get().setStars(request.getStars());
        reviewRepository.save(review.get());
    }


    @Override
    public void deleteReview(String token, Long id, Long idd) {
        Optional<Review> review = reviewRepository.findById(idd);
        if(review.isEmpty())
            throw new BadRequestException("This review doesn't exist!");
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty())
            throw new BadRequestException("This product doesn't exist!");
        User user = authService.getUserFromToken(token);
        if(review.get().getUser() != user)
            throw new BadCredentialsException("You're not allowed to delete this review!");
        if(review.get().getProduct() != product.get())
            throw new BadRequestException("This product doesn't have this review!");

        user.getReviews().remove(review.get());
        product.get().getReviews().remove(review.get());

        userRepository.save(user);
        productRepository.save(product.get());

        review.get().setUser(null);
        review.get().setProduct(null);

        reviewRepository.delete(review.get());

    }

}
