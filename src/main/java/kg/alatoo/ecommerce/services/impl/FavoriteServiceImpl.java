package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.product.ProductResponse;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.mappers.ProductMapper;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final AuthService authService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Override
    public void add(String token, Long id) {
        User user = authService.getUserFromToken(token);
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty())
            throw new BadRequestException("Product doesn't exist!");

        List<Product> products = new ArrayList<>();
        if(user.getFavorites() != null) products = user.getFavorites();
        if(products.contains(product.get()))
            throw new BadRequestException("This product already in favorites!");
        products.add(product.get());
        user.setFavorites(products);

        userRepository.save(user);
    }

    @Override
    public List<ProductResponse> all(String token) {
        User user = authService.getUserFromToken(token);
        return productMapper.toDtos(user.getFavorites());
    }

    @Override
    public void delete(String token, Long id) {
        Optional<Product> product = productRepository.findById(id);
        User user = authService.getUserFromToken(token);
        if (product.isEmpty() || !user.getFavorites().contains(product.get())) {
            throw new NotFoundException("This product doesn't exist!", HttpStatus.NOT_FOUND);
        }
        user.getFavorites().remove(product.get());
        userRepository.save(user);
    }
}
