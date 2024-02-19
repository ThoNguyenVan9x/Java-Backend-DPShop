package thonguyenvan.dpshop.entity;

import jakarta.persistence.*;
import lombok.*;
import thonguyenvan.dpshop.enums.ProductEnum;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String size;;

    @Column(nullable = false)
    private Double price;

//    @Column(nullable = false)
    private String image = "/assets/images/product-1.png";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductEnum type;
}
