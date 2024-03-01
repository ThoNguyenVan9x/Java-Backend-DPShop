package thonguyenvan.dpshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
