package thonguyenvan.dpshop.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name = "thumbnail", length = 350)
    private String thumbnail;

    private String description;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;
}


