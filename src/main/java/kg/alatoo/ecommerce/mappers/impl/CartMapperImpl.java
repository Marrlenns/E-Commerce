package kg.alatoo.ecommerce.mappers.impl;

import kg.alatoo.ecommerce.dto.cart.CartResponse;
import kg.alatoo.ecommerce.entities.Cart;
import kg.alatoo.ecommerce.entities.CartItem;
import kg.alatoo.ecommerce.mappers.CartMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartMapperImpl implements CartMapper {
    @Override
    public CartResponse toDto(Cart cart) {
        CartResponse response = new CartResponse();
        List<String> titles = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<Integer> subtotals = new ArrayList<>();
        Integer total = 0;
        List<CartItem> items = cart.getItems();
        for(CartItem item: items) {
            titles.add(item.getTitle());
            prices.add(item.getPrice());
            quantities.add(item.getQuantity());
            subtotals.add(item.getSubtotal());
            total += item.getSubtotal();
        }
        response.setTitles(titles);
        response.setPrices(prices);
        response.setQuantities(quantities);
        response.setSubtotals(subtotals);
        response.setTotal(total);

        return response;
    }
}
