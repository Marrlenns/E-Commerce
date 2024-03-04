package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
