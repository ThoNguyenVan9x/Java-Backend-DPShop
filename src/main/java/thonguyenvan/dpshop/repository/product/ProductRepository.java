package thonguyenvan.dpshop.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thonguyenvan.dpshop.dto.ProductDTO;
import thonguyenvan.dpshop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select new thonguyenvan.dpshop.dto.ProductDTO(p.id, p.name, p.material, p.size, p.price, p.countInStock, p.discount, p.rating, p.image, p.type) from Product p where upper(p.name) like upper(concat('%', :searchText, '%') ) OR upper(p.type) = upper(:searchType) ")
    Page<ProductDTO> getProductList(String searchText, String searchType, Pageable pageable);

    @Query("select new thonguyenvan.dpshop.dto.ProductDTO(p.id, p.name, p.material, p.size, p.price, p.countInStock, p.discount, p.rating, p.image, p.type) from Product p")
    Page<ProductDTO> getProductList(Pageable pageable);

    @Query("select new thonguyenvan.dpshop.dto.ProductDTO(p.id, p.name, p.material, p.size, p.price, p.countInStock, p.discount, p.rating, p.image, p.type) from Product p where p.id = :id")
    ProductDTO getProductById(Integer id);


}
