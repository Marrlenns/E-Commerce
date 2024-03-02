package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.cart.AddToCartRequest;
import kg.alatoo.ecommerce.dto.cart.CartResponse;
import kg.alatoo.ecommerce.entities.*;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.mappers.CartMapper;
import kg.alatoo.ecommerce.repositories.*;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.CartService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final OrderRepository orderRepository;
    private final ImageRepository imageRepository;

    @Override
    public void add(AddToCartRequest request, String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        if(cart.getItems().size() == 10)
            throw new BadRequestException("Your card is full!");
        Optional<Product> product = productRepository.findById(request.getProductId());
        if(product.isEmpty()) {
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist!");
        }
        Optional<CartItem> isItem = cartItemRepository.findBySkuAndCart(product.get().getSku(), cart);
        if(isItem.isPresent() && isItem.get().getCart() == cart) {
            throw new BadRequestException("You already added this product to your cart!");
        }
        CartItem item = new CartItem();
        item.setSku(product.get().getSku());
        item.setTitle(product.get().getTitle());
        item.setPrice(product.get().getPrice());
        item.setQuantity(request.getQuantity());
        item.setSubtotal(product.get().getPrice() * request.getQuantity());
        item.setCart(user.getCart());
        if(product.get().getImage() != null)
            System.out.println(user.getUsername() + " " + 1);
            item.setImage(product.get().getImage());

        CartItem cartItem = cartItemRepository.saveAndFlush(item);

        List<CartItem> items = new ArrayList<>();
        if(cart.getItems() != null) items = cart.getItems();
        items.add(cartItem);
        cart.setItems(items);
        cartRepository.save(cart);
        Image image = product.get().getImage();
        items = new ArrayList<>();
        if(image.getItems() != null) items = image.getItems();
        items.add(cartItem);
        image.setItems(items);
        imageRepository.save(image);
    }

    @Override
    public void update(AddToCartRequest request, String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        Optional<CartItem> item = cartItemRepository.findById(request.getProductId());
        if(item.isEmpty())
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist!");
        if(item.get().getCart() != cart)
            throw new BadRequestException("Product with id: " + request.getProductId() + " - doesn't exist in your cart!");
        item.get().setQuantity(request.getQuantity());
        item.get().setSubtotal(request.getQuantity() * item.get().getPrice());
        cartItemRepository.save(item.get());
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
        cart.getItems().remove(item.get());
        cartRepository.save(cart);
        item.get().setCart(null);

        Image image = item.get().getImage();
        List<CartItem> items = image.getItems();
        items.remove(item.get());
        image.setItems(items);
        imageRepository.save(image);
        item.get().setImage(null);
        cartItemRepository.delete(item.get());
    }

    @Override
    public CartResponse show(String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        return cartMapper.toDto(cart);
    }

    @Override
    public void buy(String token) {
        User user = authService.getUserFromToken(token);
        Cart cart = cartRepository.findById(user.getId()).get();
        if(cart.getItems().size() == 0)
            throw new BadRequestException("Your cart is empty!");
        List<CartItem> items = cart.getItems();
        for (CartItem item: items){
            addOrderToUser(item, user);
            item.setCart(null);
            if(item.getImage() != null){
                List<CartItem> items1 = item.getImage().getItems();
                items1.remove(item);
                item.getImage().setItems(items1);
                imageRepository.save(item.getImage());
                item.setImage(null);
            }
        }

        cart.setItems(null);
        cartRepository.save(cart);
        for (CartItem item: items) cartItemRepository.delete(item);

    }

    public void addOrderToUser(CartItem item, User user){
        Order order = new Order();
        order.setTitle(item.getTitle());
        order.setSku(item.getSku());
        order.setQuantity(item.getQuantity());
        order.setTotal(item.getSubtotal());
        order.setPrice(item.getPrice());
        order.setCreateDate(LocalDateTime.now());
        order.setUser(user);
        if(item.getImage() != null) {
            Image image = item.getImage();
            order.setImage(image);
            List<Order> orders = new ArrayList<>();
            if(image.getOrders() != null) orders = image.getOrders();
            orders.add(orderRepository.saveAndFlush(order));
            image.setOrders(orders);
            imageRepository.save(image);
        }
        Order order1 = orderRepository.saveAndFlush(order);
        List<Order> orders = new ArrayList<>();
        if(!user.getOrders().isEmpty())
            orders = user.getOrders();
        orders.add(order1);
        user.setOrders(orders);
        userRepository.save(user);
    }
}
