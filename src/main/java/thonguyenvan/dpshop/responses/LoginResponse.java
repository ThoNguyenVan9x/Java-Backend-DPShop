package thonguyenvan.dpshop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import thonguyenvan.dpshop.models.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    private LoginUserResponse user;
}
