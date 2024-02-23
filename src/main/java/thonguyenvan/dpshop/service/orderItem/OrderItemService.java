package thonguyenvan.dpshop.service.orderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.entity.OrderItem;

public interface OrderItemService{

    OrderItem addOrderItem(OrderItem  orderItem);
}
