package thonguyenvan.dpshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.models.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}
