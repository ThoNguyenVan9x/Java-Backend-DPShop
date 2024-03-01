package thonguyenvan.dpshop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import thonguyenvan.dpshop.models.Product;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends BaseResponse {

    private Long id;

    private String name;

    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_name")
    private String category;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse =  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
