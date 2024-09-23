package com.example.demo.repository;

import com.example.demo.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    @Query("SELECT d FROM Document d WHERE TYPE(d) = Book")
    List<Document> findByTypeBook(String type);
    @Query("SELECT d FROM Document d WHERE TYPE(d) = Magazine")
    List<Document> findByTypeMagazine(String type);
    @Query("SELECT d FROM Document d WHERE TYPE(d) = Report")
    List<Document> findByTypeReport(String type);





}
