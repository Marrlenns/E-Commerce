package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
