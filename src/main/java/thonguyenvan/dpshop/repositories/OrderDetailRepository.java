package thonguyenvan.dpshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.models.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);
}
