package thonguyenvan.dpshop.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thonguyenvan.dpshop.dto.ProductDTO;

public interface ProductService {

    Page<ProductDTO> getProductList(String search, String searchType, Pageable pageable);

    ProductDTO getDetailProduct(Integer id);
}
