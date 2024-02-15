package kg.alatoo.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_item_table")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer price;
    private Integer quantity;
    private Integer subtotal;
    private String sku;

    @ManyToOne
    private Cart cart;
}
