package thonguyenvan.dpshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @NotBlank(message = "Pass is required")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("facebook_account_id")
    private Integer facebookAccountId = 0;

    @JsonProperty("google_account_id")
    private Integer googleAccountId = 0;

    @JsonProperty("role_id")
    @NotNull(message = "Role id is required")
    private Long roleId;
}
