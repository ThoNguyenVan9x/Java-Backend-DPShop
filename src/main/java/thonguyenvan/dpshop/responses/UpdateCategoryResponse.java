package thonguyenvan.dpshop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateCategoryResponse {
        @JsonProperty("message")
        private String message;

}
