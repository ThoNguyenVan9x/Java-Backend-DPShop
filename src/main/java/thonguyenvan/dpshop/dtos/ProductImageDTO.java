package thonguyenvan.dpshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {

    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Image's name must be between 5 to 200 character")
    private String imageUrl;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;
}
