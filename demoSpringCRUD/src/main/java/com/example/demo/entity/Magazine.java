package com.example.demo.entity;


import jakarta.persistence.Entity;

@Entity
public class Magazine extends Document {
    private String banPhatHanh;
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
