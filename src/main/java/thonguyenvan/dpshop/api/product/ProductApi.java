package thonguyenvan.dpshop.api.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import thonguyenvan.dpshop.dto.ProductDTO;
import thonguyenvan.dpshop.entity.Product;
import thonguyenvan.dpshop.service.product.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDTO> getListProduct(@RequestParam(defaultValue = "0",required = false, value="pageIndex") Integer pageIndex,
                                           @RequestParam(defaultValue = "8", required = false, value="pageSize") Integer pageSize,
                                           @RequestParam(required = false, value="searchType") String searchType,
                                           @RequestParam(required = false, value="search") String search) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return productService.getProductList(search, searchType, pageable);
    }

    @GetMapping("/{id}")
    public ProductDTO getDetailProduct(@PathVariable Integer id) {
        return productService.getDetailProduct(id);
    }

    @PostMapping
    public Product AddNewProduct(@RequestBody Product product) {
       return productService.addNewProduct(product);
    }
}
