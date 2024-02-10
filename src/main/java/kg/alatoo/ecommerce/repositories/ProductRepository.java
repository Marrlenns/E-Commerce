package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    List<Product> findAllByUser(User user);
}
