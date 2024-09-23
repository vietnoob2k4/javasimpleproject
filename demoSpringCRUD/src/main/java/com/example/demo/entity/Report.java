package com.example.demo.entity;


import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Report extends Document{
    private LocalDate ngayPhatHanh;

    public LocalDate getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(LocalDate ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
    }
}
