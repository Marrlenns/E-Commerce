package kg.alatoo.ecommerce.dto.user;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import kg.alatoo.ecommerce.entities.Cart;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.Review;
import kg.alatoo.ecommerce.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String country;
    private String streetAddress;
    private String city;
    private String province;
    private String zipCode;
    private String phone;
    private String additionalInfo;
}
