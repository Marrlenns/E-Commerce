package kg.alatoo.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_item_table")
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    private String product;
    private Integer price;
    private Integer quantity;
    private Integer subtotal;

    @ManyToOne
    private Cart cart;
}
