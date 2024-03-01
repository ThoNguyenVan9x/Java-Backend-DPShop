package thonguyenvan.dpshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.dtos.ProductDTO;
import thonguyenvan.dpshop.dtos.ProductImageDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.exeptions.InvalidParamException;
import thonguyenvan.dpshop.models.Category;
import thonguyenvan.dpshop.models.Product;
import thonguyenvan.dpshop.models.ProductImage;
import thonguyenvan.dpshop.repositories.CategoryRepository;
import thonguyenvan.dpshop.repositories.ProductImageRepository;
import thonguyenvan.dpshop.repositories.ProductRepository;
import thonguyenvan.dpshop.responses.ProductResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Can not find category with id = " + productDTO.getCategoryId()));
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .description(productDTO.getDescription())

                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find category with id = " + id));
        Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Can not find category with id = " + productDTO.getCategoryId()));
        if (productDTO.getThumbnail() != null) {
            existProduct.setThumbnail(productDTO.getThumbnail());
        }
        existProduct.setName(productDTO.getName());
        existProduct.setPrice(productDTO.getPrice());
        existProduct.setDescription(productDTO.getDescription());
        existProduct.setCategory(existingCategory);
        return productRepository.save(existProduct);
    }

    @Override
    public Product getProductById(long productId) throws DataNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id = " + productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        // lay san pham theo trang(page) vaf gioi han(limit)
        return productRepository.findAll(keyword, categoryId, pageRequest).map(ProductResponse::fromProduct);
    }

//    @Override
//    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
//        Product existingProduct = getProductById(id);
//        if (existingProduct != null) {
//            // copy cac thuoc tinh tu dto => product.
//            // co the su dung modelMapper
//            existingProduct.setName(productDTO.getName());
//            Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Can not find category with id = " + productDTO.getCategoryId()));
//            existingProduct.setCategory(existingCategory);
//            existingProduct.setPrice(productDTO.getPrice());
//            existingProduct.setDescription(productDTO.getDescription());
////            existingProduct.setThumbnail(productDTO.getThumbnail());
//            return productRepository.save(existingProduct);
//        }
//        return null;
//    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot found product with id = " + productImageDTO.getProductId()));
        ProductImage productImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // khong cho insert qua 5 anh cho 1 san pham.
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be <= " +
                    ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(productImage);
    }
}
