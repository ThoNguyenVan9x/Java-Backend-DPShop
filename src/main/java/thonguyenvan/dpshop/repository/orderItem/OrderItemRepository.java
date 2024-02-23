package thonguyenvan.dpshop.repository.orderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
