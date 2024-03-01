package thonguyenvan.dpshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.models.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}
