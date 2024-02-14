package kg.alatoo.ecommerce.dto.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private List<String> titles;
    private List<Integer> prices;
    private List<Integer> quantities;
    private List<Integer> subtotals;
    private Integer total;
}
