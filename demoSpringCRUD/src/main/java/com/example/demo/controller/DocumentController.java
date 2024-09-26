package com.example.demo.controller;

import com.example.demo.entity.Document;
import com.example.demo.exception.DocumentNotFoundResponse;
import com.example.demo.service.DocumentService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
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
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentService.getDocumentById(id);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get());
        } else {
            // Trả về thông báo "document not found"
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DocumentNotFoundResponse("Document not found with ID: " + id));
        }
    }


    @PostMapping
    public ResponseEntity<Document> createDocument(@Valid @RequestBody JsonNode documentNode) {
        Document savedDocument = documentService.saveDocument(documentNode);
        return ResponseEntity.ok(savedDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody JsonNode documentNode) {
        Document existingDocument = documentService.getDocumentById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

        existingDocument = documentService.saveDocument(documentNode);
        existingDocument.setId(id);

        return ResponseEntity.ok(existingDocument);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok("Xóa thành công tài liệu");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found, delete failed");
        }
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<?> getDocumentsByType(@PathVariable String type) {
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
                    return ResponseEntity.badRequest().body("Invalid document type");
            }

            // Check if the document list is empty
            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DocumentNotFoundResponse("document not found"));
            }

            return ResponseEntity.ok(documents);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new DocumentNotFoundResponse(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DocumentNotFoundResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

}

