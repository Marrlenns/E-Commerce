package kg.alatoo.ecommerce.mappers;

import kg.alatoo.ecommerce.dto.user.ProfileResponse;
import kg.alatoo.ecommerce.entities.User;

public interface UserMapper {
    ProfileResponse toDto(User user);
}
