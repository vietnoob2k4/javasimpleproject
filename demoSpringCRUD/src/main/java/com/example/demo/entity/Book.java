package com.example.demo.entity;


import jakarta.persistence.Entity;

@Entity
public class Book extends Document {
    private String tacGia;
    private String soTrang;

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(String soTrang) {
        this.soTrang = soTrang;
    }
}
