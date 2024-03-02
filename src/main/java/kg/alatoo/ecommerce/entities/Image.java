package kg.alatoo.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "path")
    private String path;

    @OneToOne
    private Product product;

    @OneToMany
    private List<CartItem> items;

    @OneToMany
    private List<Order> orders;

}
