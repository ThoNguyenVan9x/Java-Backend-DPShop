package thonguyenvan.dpshop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thonguyenvan.dpshop.models.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @Query("select o from Order o where " +
            "(:keyword is null or :keyword = '' or o.fullName like %:keyword% or o.phoneNumber like %:keyword%)")
    Page<Order> findAllListOrders(String keyword, Pageable pageable);
}


//@Query("select p from Product p where " +
//            "(:categoryId is null or :categoryId = 0 or p.category.id = :categoryId) " +
//            "and (:keyword is null or :keyword = '' or p.name like %:keyword% or p.description like %:keyword%)")