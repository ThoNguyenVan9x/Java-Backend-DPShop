package thonguyenvan.dpshop.entity;

import jakarta.persistence.*;
import lombok.*;
import thonguyenvan.dpshop.enums.OrderStatusEnum;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private String note;

    private LocalDate orderDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatusEnum status;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
