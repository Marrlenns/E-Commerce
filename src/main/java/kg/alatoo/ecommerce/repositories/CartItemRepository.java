package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
