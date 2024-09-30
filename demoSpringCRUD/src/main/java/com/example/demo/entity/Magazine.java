package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Magazine extends Document {
    @NotBlank(message = "ban phat hanh khong duoc bo trong")
    private String banPhatHanh;
    @NotBlank(message = "thang phat hanh khong duoc bo trong")
    private String thangPhatHanh;


}
