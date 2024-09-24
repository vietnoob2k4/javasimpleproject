package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class, name = "book"),
        @JsonSubTypes.Type(value = Magazine.class, name = "magazine"),
        @JsonSubTypes.Type(value = Report.class, name = "report")
})
public abstract class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nhà xuất bản không được bỏ trống")
    private String nhaXuatBan;

    @NotNull(message = "Số bản xuất không được bỏ trống")
    private Integer soBanXuat; // Chuyển sang Integer để có thể kiểm tra NotNull



    public Document() {}

    public Document(String nhaXuatBan, int soBanXuat,String type) {
        this.nhaXuatBan = nhaXuatBan;
        this.soBanXuat = soBanXuat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public void setNhaXuatBan(String nhaXuatBan) {
        this.nhaXuatBan = nhaXuatBan;
    }

    public Integer getSoBanXuat() {
        return soBanXuat;
    }

    public void setSoBanXuat(Integer soBanXuat) {
        this.soBanXuat = soBanXuat;
    }


}