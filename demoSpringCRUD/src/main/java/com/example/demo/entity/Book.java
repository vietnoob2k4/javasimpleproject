package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Book extends Document {
    @NotBlank(message = "tac gia khong duoc bo trong")
    private String tacGia;
    @Positive(message = "so trang phai la so duong")
    private int soTrang;

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public int getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(int soTrang) {
        this.soTrang = soTrang;
    }
}
