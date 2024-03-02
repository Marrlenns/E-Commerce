package kg.alatoo.ecommerce.services;

import kg.alatoo.ecommerce.dto.order.OrderDetailResponse;
import kg.alatoo.ecommerce.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> all(String token);

    OrderDetailResponse getById(String token, Long id);

    void delete(String token, Long id);
}
