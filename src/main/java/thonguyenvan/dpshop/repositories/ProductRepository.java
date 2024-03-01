package thonguyenvan.dpshop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thonguyenvan.dpshop.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query("select p from Product p where " +
            "(:categoryId is null or :categoryId = 0 or p.category.id = :categoryId) " +
            "and (:keyword is null or :keyword = '' or p.name like %:keyword% or p.description like %:keyword%)")
    Page<Product> findAll(String keyword, Long categoryId, Pageable pageable);
}
