package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
public class Report extends Document{
    @NotBlank(message = "ngay phat hanh khong duoc bo trong")
    private String ngayPhatHanh;

    public String getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(String ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
    }
}
