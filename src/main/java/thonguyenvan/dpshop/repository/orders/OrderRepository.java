package thonguyenvan.dpshop.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
}
