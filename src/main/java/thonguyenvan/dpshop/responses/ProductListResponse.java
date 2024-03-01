package thonguyenvan.dpshop.responses;

import lombok.*;
import thonguyenvan.dpshop.models.Product;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {

    private List<ProductResponse> productResponses;

    private int totalPages;
}
