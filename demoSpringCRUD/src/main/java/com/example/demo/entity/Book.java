package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends Document {
    @NotBlank(message = "tac gia khong duoc bo trong")
    private String tacGia;
    @Positive(message = "so trang phai la so duong")
    private int soTrang;


}
