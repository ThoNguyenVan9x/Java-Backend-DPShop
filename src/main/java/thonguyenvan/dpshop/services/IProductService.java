package thonguyenvan.dpshop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import thonguyenvan.dpshop.dtos.ProductDTO;
import thonguyenvan.dpshop.dtos.ProductImageDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.exeptions.InvalidParamException;
import thonguyenvan.dpshop.models.Product;
import thonguyenvan.dpshop.models.ProductImage;
import thonguyenvan.dpshop.responses.ProductResponse;

public interface IProductService {

    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

//    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamException;
}
