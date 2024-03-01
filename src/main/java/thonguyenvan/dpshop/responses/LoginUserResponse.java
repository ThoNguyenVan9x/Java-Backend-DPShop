package thonguyenvan.dpshop.responses;

import jakarta.persistence.*;
import lombok.*;
import thonguyenvan.dpshop.models.Role;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

    private String role;
}
