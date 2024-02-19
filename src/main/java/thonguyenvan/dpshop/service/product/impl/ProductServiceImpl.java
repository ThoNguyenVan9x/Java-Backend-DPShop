package thonguyenvan.dpshop.service.product.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thonguyenvan.dpshop.dto.ProductDTO;
import thonguyenvan.dpshop.entity.Product;
import thonguyenvan.dpshop.repository.product.ProductRepository;
import thonguyenvan.dpshop.service.product.ProductService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductDTO> getProductList(String search, String searchType, Pageable pageable) {
        return productRepository.getProductList(search, searchType, pageable);
    }

    @Override
    public ProductDTO getDetailProduct(Integer id) {
        return productRepository.getProductById(id);
    }

    @Override
    @Transactional
    public Product addNewProduct(Product product) {
        return productRepository.save(product);
    }
}
