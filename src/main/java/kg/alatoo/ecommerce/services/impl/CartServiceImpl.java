package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.cart.AddToCartRequest;
import kg.alatoo.ecommerce.entities.Cart;
import kg.alatoo.ecommerce.entities.CartItem;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.repositories.CartItemRepository;
import kg.alatoo.ecommerce.repositories.CartRepository;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public void add(AddToCartRequest request, String token) {
        User user = authService.getUserFromToken(token);
        Optional<Product> product = productRepository.findById(request.getProductId());
        if(product.isEmpty())
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist!");

        CartItem item = new CartItem();
        item.setProduct(product.get().getTitle());
        item.setPrice(product.get().getPrice());
        item.setQuantity(request.getQuantity());
        item.setSubtotal(product.get().getPrice() * request.getQuantity());

        CartItem cartItem = cartItemRepository.saveAndFlush(item);

        Cart cart = cartRepository.findById(user.getId()).get();
        List<CartItem> items = new ArrayList<>();
        if(cart.getItems() != null) items = cart.getItems();
        items.add(cartItem);
        cart.setItems(items);
        cartRepository.save(cart);
    }
}
