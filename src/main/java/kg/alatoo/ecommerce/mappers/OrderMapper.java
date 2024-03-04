package kg.alatoo.ecommerce.mappers;

import kg.alatoo.ecommerce.dto.order.OrderDetailResponse;
import kg.alatoo.ecommerce.dto.order.OrderResponse;
import kg.alatoo.ecommerce.entities.Order;

import java.util.List;

public interface OrderMapper {
    List<OrderResponse> toDtos(List<Order> orders);

    OrderDetailResponse toDetailDto(Order order);
}
