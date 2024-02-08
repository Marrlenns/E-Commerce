package kg.alatoo.ecommerce.entities;

import jakarta.persistence.*;
import kg.alatoo.ecommerce.enums.Color;
import kg.alatoo.ecommerce.enums.Size;
import kg.alatoo.ecommerce.enums.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product_table")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer price;
    private String description;

    @ElementCollection(targetClass = Color.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<Color> colors;

    @ElementCollection(targetClass = Tag.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<Tag> tags;

    @ElementCollection(targetClass = Size.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<Size> sizes;

    private String sku;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

}
