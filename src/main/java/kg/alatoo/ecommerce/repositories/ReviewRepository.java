package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.Review;
import kg.alatoo.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserAndProduct(User user, Product product);
}
