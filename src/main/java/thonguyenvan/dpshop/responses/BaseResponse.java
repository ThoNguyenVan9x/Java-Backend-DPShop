package thonguyenvan.dpshop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseResponse {

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
