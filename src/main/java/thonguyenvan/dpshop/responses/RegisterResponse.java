package thonguyenvan.dpshop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import thonguyenvan.dpshop.models.User;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}