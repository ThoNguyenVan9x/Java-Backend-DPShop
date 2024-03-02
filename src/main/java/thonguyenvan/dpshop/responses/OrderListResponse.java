package thonguyenvan.dpshop.responses;

import lombok.*;
import thonguyenvan.dpshop.models.Order;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {
    private List<OrderResponse> orderResponses;

    private int totalPages;
}
