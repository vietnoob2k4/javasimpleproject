package com.example.demo.service;

import com.example.demo.entity.Document;
import com.example.demo.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
//    public List<Document> findDocumentsByType(String type) {
//        List<Document> documents = documentRepository.findByType(type);
//        if (documents.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents found of type: " + type);
//        }
//        return documents;
//    }
    public List<Document> findDocumentsByTypeBook(String type) {
    List<Document> documents = documentRepository.findByTypeBook(type);
    if (documents.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents found of type: " + type);
    }
    return documents;
}
    public List<Document> findDocumentsByTypeMagazine(String type) {
        List<Document> documents = documentRepository.findByTypeMagazine(type);
        if (documents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents found of type: " + type);
        }
        return documents;
    }
    public List<Document> findDocumentsByTypeReport(String type) {
        List<Document> documents = documentRepository.findByTypeReport(type);
        if (documents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents found of type: " + type);
        }
        return documents;
    }


}
