package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.order.OrderDetailResponse;
import kg.alatoo.ecommerce.dto.order.OrderResponse;
import kg.alatoo.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public List<OrderResponse> all(@RequestHeader("Authorization") String token){
        return orderService.all(token);
    }

    @GetMapping("/{id}")
    public OrderDetailResponse detail(@RequestHeader("Authorization") String token, @PathVariable Long id){
        return orderService.getById(token, id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@RequestHeader("Authorization") String token, @PathVariable Long id){
        orderService.delete(token, id);
        return "Order deleted successfully!";
    }

}
