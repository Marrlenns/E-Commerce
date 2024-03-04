package kg.alatoo.ecommerce.repositories;

import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(String uuid);
}
