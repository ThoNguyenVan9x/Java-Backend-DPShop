package thonguyenvan.dpshop.dto;

import jakarta.persistence.Column;
import lombok.*;
import thonguyenvan.dpshop.enums.ProductEnum;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class ProductDTO{
    private Integer id;

    private String name;

    private String material;

    private String size;;

    private Double price;

    private Integer countInStock;

    private Integer discount;

    private Integer rating;

    private String image;

    private ProductEnum type;
}
