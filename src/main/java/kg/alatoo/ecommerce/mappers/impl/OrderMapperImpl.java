package kg.alatoo.ecommerce.mappers.impl;

import kg.alatoo.ecommerce.dto.order.OrderDetailResponse;
import kg.alatoo.ecommerce.dto.order.OrderResponse;
import kg.alatoo.ecommerce.entities.Order;
import kg.alatoo.ecommerce.mappers.ImageMapper;
import kg.alatoo.ecommerce.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final ImageMapper imageMapper;

    @Override
    public List<OrderResponse> toDtos(List<Order> orders) {
        List<OrderResponse> responses = new ArrayList<>();
        for(Order order: orders)
            responses.add(toDto(order));
        return responses;
    }

    @Override
    public OrderDetailResponse toDetailDto(Order order) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setId(order.getId());
        response.setSku(order.getSku());
        response.setTitle(order.getTitle());
        response.setPrice(order.getPrice());
        response.setQuantity(order.getQuantity());
        response.setCreateDate(order.getCreateDate());
        response.setTotal(order.getTotal());
        if(order.getImage() != null)
            response.setImage(imageMapper.toDetailDto(order.getImage()));
        return response;
    }

    private OrderResponse toDto(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTitle(order.getTitle());
        response.setPrice(order.getPrice());
        response.setCreateDate(order.getCreateDate());
        if(order.getImage() != null)
            response.setImageName(order.getImage().getName());
        return response;
    }
}
