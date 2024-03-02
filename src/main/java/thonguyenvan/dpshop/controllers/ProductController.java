package thonguyenvan.dpshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import thonguyenvan.dpshop.components.LocalizationUtils;
import thonguyenvan.dpshop.dtos.ProductDTO;
import thonguyenvan.dpshop.dtos.ProductImageDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.exeptions.InvalidParamException;
import thonguyenvan.dpshop.models.Product;
import thonguyenvan.dpshop.models.ProductImage;
import thonguyenvan.dpshop.responses.ProductListResponse;
import thonguyenvan.dpshop.responses.ProductResponse;
import thonguyenvan.dpshop.services.IProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("") //http:localhost:8080/api/v1/products?page=1&limit=10
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "category_id", defaultValue = "0") Long categoryId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        System.out.println("day chay vao ham get all");
        // tao Pageable tu thong tin trang va gioi han
         PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
//        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
        // lay tong so trang(total page)
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products =  productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .productResponses(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
           Product existingProduct =  productService.getProductById(productId);
           return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @PostMapping(value = "")
    public ResponseEntity<?> insertProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("object") String object)  {
//        System.out.println("image: " + image);
//        System.out.println("object: " + object);
//        System.out.println("content type: " + request.getContentType());
//
//        ProductDTO productDTO = new ProductDTO();
//        ObjectMapper mapper = new ObjectMapper();
//        productDTO = mapper.readValue(object, ProductDTO.class);
//
//        System.out.println("product dto before: " + productDTO);
//        String thumbnailUrl = storeFile(image);
//        productDTO.setThumbnail(thumbnailUrl);
//        System.out.println("product dto after: " + productDTO);

        try {
            ProductDTO productDTO = new ProductDTO();
            ObjectMapper mapper = new ObjectMapper();
            productDTO = mapper.readValue(object, ProductDTO.class);
            String thumbnailUrl = storeFile(image);
            productDTO.setThumbnail(thumbnailUrl);
            Product newProduct = productService.createProduct(productDTO);

//            if(result.hasErrors()) {
//                List<String> errorMessages = result.getFieldErrors()
//                        .stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(errorMessages);
//            }
//            String thumbnailUrl = storeFile(productDTO.getThumbnail());
//            productDTO.setThumbnailUrl(thumbnailUrl);
//            Product newProduct = productService.createProduct(productDTO);


            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
//        return null;
    }


    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("object") String object)  {
        try {
            ProductDTO productDTO = new ProductDTO();
            ObjectMapper mapper = new ObjectMapper();
            productDTO = mapper.readValue(object, ProductDTO.class);
            if (image != null) {
                String thumbnailUrl = storeFile(image);
                productDTO.setThumbnail(thumbnailUrl);
            }
            Product newProduct = productService.updateProduct(id, productDTO);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ham luu file
    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // them uuid vao truoc ten file de dam bao ten file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // duong dan den thu muc ma ban muon luu file
        Path uploadDir = Paths.get("uploads");
        // kiem tra va tao thu muc neu no chua ton tai
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // duong dan day du den file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // sao chep file vao thu muc dich, neu co se thay the
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    // ham kiem tra co phai file anh khong
    private boolean isImageFile(MultipartFile file) {
        String contenType = file.getContentType();
        return contenType != null && contenType.startsWith("image/");
    }

    // ham update image product
    @Transactional
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
                                        @ModelAttribute("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                // kiem tra kich thuoc file
                if(file.getSize() > 10*1024*1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                // kiem tra dinh dang file
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // luu file va cap nhat thumbnail trong DTO
                String filename = storeFile(file);
                // luu vao doi tuong product trong DB -> se lam sau.
                // luu vao bang product images
                ProductImage productImage =  productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidParamException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(String.format("Deleted successfully product with id = %d", id));
    }

    @Transactional
    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 1000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(0, 90000000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1, 8))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }

}
