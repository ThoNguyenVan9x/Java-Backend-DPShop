package thonguyenvan.dpshop.api.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thonguyenvan.dpshop.dto.ProductDTO;
import thonguyenvan.dpshop.entity.Product;
import thonguyenvan.dpshop.service.product.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @GetMapping("/list")
    public Page<ProductDTO> getListProduct(@RequestParam(defaultValue = "0",required = false, value="pageIndex") Integer pageIndex,
                                           @RequestParam(defaultValue = "8", required = false, value="pageSize") Integer pageSize,
                                           @RequestParam(required = false, value="searchType") String searchType,
                                           @RequestParam(required = false, value="searchText") String searchText) {
        System.out.println("search type: " + searchType);
        System.out.println("search text: " + searchText);
        if(searchText == "") searchText = null;
        if(searchType == "") searchType = null;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        if(searchText == null && searchType == null){
            System.out.println("chay vao case 2 null");
            return productService.getProductList(pageable);
        }
        System.out.println("chay vao case 1 or 0 null");
        return productService.getProductList(searchText, searchType, pageable);
    }

    @GetMapping("/detail/{id}")
    public ProductDTO getDetailProduct(@PathVariable Integer id) {
        return productService.getDetailProduct(id);
    }

    @PostMapping("/add")
    public Product addNewProduct(@RequestBody Product product) {
        System.out.println("chạy vào đây rồi!");
        System.out.println("product: " + product.getId());
        System.out.println("product: " + product.getName());
        System.out.println("product: " + product.getMaterial());
        System.out.println("product: " + product.getPrice());
       return productService.addNewProduct(product);
    }

    @PutMapping("/edit")
    public Product updateProduct(@RequestBody Product product) {
        System.out.println( "product id: " + product.getId());
        return productService.updateProduct(product);
    }
}
