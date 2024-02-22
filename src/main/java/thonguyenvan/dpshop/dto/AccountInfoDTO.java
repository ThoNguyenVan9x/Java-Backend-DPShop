package thonguyenvan.dpshop.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thonguyenvan.dpshop.enums.Role;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDTO {

    private String username;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private Role role;

   private String token;
}
