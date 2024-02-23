package thonguyenvan.dpshop.dto;

import lombok.*;
import thonguyenvan.dpshop.entity.OrderItem;
import thonguyenvan.dpshop.entity.Orders;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class OrderDTO {
    private List<OrderItem> order;
    private HashMap customer;
}
