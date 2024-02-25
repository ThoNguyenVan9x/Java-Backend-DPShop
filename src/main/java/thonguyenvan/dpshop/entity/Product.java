package thonguyenvan.dpshop.entity;

import jakarta.persistence.*;
import lombok.*;
import thonguyenvan.dpshop.enums.ProductEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

//    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
    private String material;

//    @Column(nullable = false)
    private String size;;

//    @Column(nullable = false)
    private Double price;

    private Integer countInStock = 100;

//    @Column(nullable = false)
    private Integer discount = 0;

//    @Column(nullable = false)
    private String image = "/assets/images/product-1.png";

    private Integer rating = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductEnum type;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
