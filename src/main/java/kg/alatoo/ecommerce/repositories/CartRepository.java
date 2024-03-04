package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.Cart;
import kg.alatoo.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
