package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.cart.AddToCartRequest;
import kg.alatoo.ecommerce.dto.cart.CartResponse;
import kg.alatoo.ecommerce.entities.Cart;
import kg.alatoo.ecommerce.entities.CartItem;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.mappers.CartMapper;
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
    private final CartMapper cartMapper;

    @Override
    public void add(AddToCartRequest request, String token) {
        User user = authService.getUserFromToken(token);
        Optional<Product> product = productRepository.findById(request.getProductId());
        if(product.isEmpty())
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist!");

        CartItem item = new CartItem();
        item.setTitle(product.get().getTitle());
        item.setPrice(product.get().getPrice());
        item.setQuantity(request.getQuantity());
        item.setSubtotal(product.get().getPrice() * request.getQuantity());
        item.setCart(user.getCart());

        CartItem cartItem = cartItemRepository.saveAndFlush(item);

        Cart cart = cartRepository.findById(user.getId()).get();
        List<CartItem> items = new ArrayList<>();
        if(cart.getItems() != null) items = cart.getItems();
        items.add(cartItem);
        cart.setItems(items);
        cartRepository.save(cart);
    }

    @Override
    public void update(AddToCartRequest request, String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        Optional<CartItem> product = cartItemRepository.findById(request.getProductId());
        if(product.isEmpty())
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist!");
        List<CartItem> items = cart.getItems();
        boolean flag = false;
        for(CartItem item: items)
            if(item.getTitle() == product.get().getTitle()){
                item.setQuantity(request.getQuantity());
                flag = true;
                cartItemRepository.save(item);
                break;
            }
        if(!flag)
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist in your cart!");
    }

    @Override
    public void delete(AddToCartRequest request, String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        Optional<CartItem> item = cartItemRepository.findById(request.getProductId());
        if(item.isEmpty())
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist!");
        if(item.get().getCart() != cart)
            throw new BadRequestException("You can't delete this product!");
        List<CartItem> items = cart.getItems();
        List<CartItem> newItems = new ArrayList<>();
        for(CartItem cartItem: items)
            if(cartItem != item.get())
                newItems.add(cartItem);
        cart.setItems(newItems);
        cartRepository.save(cart);
        item.get().setCart(null);
        cartItemRepository.delete(item.get());
    }

    @Override
    public CartResponse show(String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        return cartMapper.toDto(cart);
    }
}
