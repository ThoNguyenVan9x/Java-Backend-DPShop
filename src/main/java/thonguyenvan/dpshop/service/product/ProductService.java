package thonguyenvan.dpshop.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thonguyenvan.dpshop.dto.ProductDTO;
import thonguyenvan.dpshop.entity.Product;

import java.util.Optional;

public interface ProductService {

    Page<ProductDTO> getProductList(String searchText, String searchType, Pageable pageable);

    Page<ProductDTO> getProductList(Pageable pageable);

    ProductDTO getDetailProduct(Integer id);

    Optional<Product> getProductById(Integer id);

    Product addNewProduct(Product product);

    Product updateProduct(Product product);
}
