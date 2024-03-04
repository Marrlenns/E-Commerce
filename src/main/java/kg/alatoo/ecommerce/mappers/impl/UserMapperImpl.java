package kg.alatoo.ecommerce.mappers.impl;

import kg.alatoo.ecommerce.dto.user.ProfileResponse;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public ProfileResponse toDto(User user) {
        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCompanyName(user.getCompanyName());
        response.setCountry(user.getCountry());
        response.setStreetAddress(user.getStreetAddress());
        response.setCity(user.getCity());
        response.setProvince(user.getProvince());
        response.setZipCode(user.getZipCode());
        response.setPhone(user.getPhone());
        response.setAdditionalInfo(user.getAdditionalInfo());
        return response;
    }
}
