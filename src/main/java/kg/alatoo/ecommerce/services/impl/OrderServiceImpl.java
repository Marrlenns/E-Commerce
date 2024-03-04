package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.order.OrderDetailResponse;
import kg.alatoo.ecommerce.dto.order.OrderResponse;
import kg.alatoo.ecommerce.entities.Image;
import kg.alatoo.ecommerce.entities.Order;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.mappers.OrderMapper;
import kg.alatoo.ecommerce.repositories.OrderRepository;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AuthService authService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderResponse> all(String token) {
        User user = authService.getUserFromToken(token);
        return orderMapper.toDtos(user.getOrders());
    }

    @Override
    public OrderDetailResponse getById(String token, Long id) {
        User user = authService.getUserFromToken(token);
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty() || order.get().getUser() != user)
            throw new NotFoundException("Order not found!", HttpStatus.NOT_FOUND);
        return orderMapper.toDetailDto(order.get());
    }

    @Override
    public void delete(String token, Long id) {
        User user = authService.getUserFromToken(token);
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty() || order.get().getUser() != user)
            throw new NotFoundException("Order not found!", HttpStatus.NOT_FOUND);
        List<Order> orders = user.getOrders();
        orders.remove(order.get());
        order.get().setUser(null);

        if(order.get().getImage() != null) {
            Image image = order.get().getImage();
            orders = image.getOrders();
            orders.remove(order.get());
            order.get().setImage(null);
        }

        orderRepository.delete(order.get());

        userRepository.save(user);
    }
}
