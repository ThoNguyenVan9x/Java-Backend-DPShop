package thonguyenvan.dpshop.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @NotEmpty(message = "Category name must be not empty!")
    private String name;
}
