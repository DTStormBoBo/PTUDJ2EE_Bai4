package com.example.ptudj2ee_bai4.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 2, max = 100, message = "Tên sản phẩm từ 2 - 100 ký tự")
    private String productName;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 1, message = "Giá sản phẩm phải từ 1 - 9999999")
    @Max(value = 9999999, message = "Giá sản phẩm phải từ 1 - 9999999")
    private Double price;

    private String image; // Sẽ được set sau khi upload file

    private Integer categoryId;

    private String categoryName;
}