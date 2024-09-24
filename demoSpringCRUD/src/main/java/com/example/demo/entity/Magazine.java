package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Magazine extends Document {
    @NotBlank(message = "ban phat hanh khong duoc bo trong")
    private String banPhatHanh;
    @NotBlank(message = "thang phat hanh khong duoc bo trong")
    private String thangPhatHanh;

    public String getBanPhatHanh() {
        return banPhatHanh;
    }

    public void setBanPhatHanh(String banPhatHanh) {
        this.banPhatHanh = banPhatHanh;
    }

    public String getThangPhatHanh() {
        return thangPhatHanh;
    }

    public void setThangPhatHanh(String thangPhatHanh) {
        this.thangPhatHanh = thangPhatHanh;
    }
}
