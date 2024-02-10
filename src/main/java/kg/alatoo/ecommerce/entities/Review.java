package kg.alatoo.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review_table")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Integer stars;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;
}
