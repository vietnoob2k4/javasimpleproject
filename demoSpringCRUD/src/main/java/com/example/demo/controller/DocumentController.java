package com.example.demo.controller;

import com.example.demo.entity.Document;
import com.example.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentService.getDocumentById(id);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        if (!documentService.getDocumentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        document.setId(id);
        Document updatedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(updatedDocument);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        if (!documentService.getDocumentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
//    @GetMapping("/type/report")
//    public ResponseEntity<List<Document>> getReport() {
//        List<Document> reports = documentService.findDocumentsByType("report");
//        return ResponseEntity.ok(reports);
//    }
//    @GetMapping("/type/book")
//    public ResponseEntity<List<Document>> getBook() {
//        List<Document> books = documentService.findDocumentsByType("book");
//        return ResponseEntity.ok(books);
//    }
//    @GetMapping("/type/magazine")
//    public ResponseEntity<List<Document>> getMagazine() {
//        List<Document> magazines = documentService.findDocumentsByType("magazine");
//        return ResponseEntity.ok(magazines);
//    }
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Document>> getDocumentsByType(@PathVariable String type) {
        List<Document> documents;
        try {
            switch (type.toLowerCase()){
                case "book":
                    documents = documentService.findDocumentsByTypeBook(type);
                    break;
                case "magazine":
                    documents = documentService.findDocumentsByTypeMagazine(type);
                    break;
                case "report":
                    documents = documentService.findDocumentsByTypeReport(type);
                    break;

                default:
                    return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(documents);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
}

}