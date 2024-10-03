package com.example.demo.controller;

import com.example.demo.entity.Document;
import com.example.demo.exception.ApiResponse;
import com.example.demo.exception.DocumentNotFoundResponse;
import com.example.demo.service.DocumentService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/documents")
public class DocumentController {


    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);

            return ResponseEntity.ok(document);

    }


    @PostMapping
    public ResponseEntity<Document> createDocument(@Valid @RequestBody JsonNode documentNode) {
        Document savedDocument = documentService.saveDocument(documentNode);
        return ResponseEntity.ok(savedDocument);
    }

//    @PostMapping
//    public ApiResponse<Document> createDocument(@RequestBody @Valid JsonNode documentNode) {
//        ApiResponse<Document> apiResponse = new ApiResponse<>();
//        // Lưu tài liệu và gán kết quả vào apiResponse
//        Document savedDocument = documentService.saveDocument(documentNode);
//        apiResponse.setResult(savedDocument);
//        return apiResponse;
//    }


    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody JsonNode documentNode) {
        Document existingDocument = documentService.getDocumentById(id);


        existingDocument = documentService.saveDocument(documentNode);
        existingDocument.setId(id);

        return ResponseEntity.ok(existingDocument);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {

            documentService.deleteDocument(id);
            return ResponseEntity.ok("Xóa thành công tài liệu có id:" + id);

    }


    @GetMapping("/type/{type}")
    public ResponseEntity<?> getDocumentsByType(@PathVariable String type) {

            List<Document> documents = documentService.findDocumentsByType(type);


            return ResponseEntity.ok(documents);

    }
    @GetMapping("/export")
    public ResponseEntity<String> exportDocument() throws IOException {
        List<Document> documents = documentService.getAllDocuments();
        String filePath = "C:/Users/vietn/Downloads/documents.xlsx";
        documentService.exportExcel(documents, filePath);
        return new ResponseEntity<>("File saved to " + filePath, HttpStatus.OK);
    }
    @PostMapping("/import")
    public ResponseEntity<String> importDocument(@RequestParam("file") MultipartFile file) {
        documentService.ImportExcel(file);
        return new ResponseEntity<>("File uploaded and processed successfully.", HttpStatus.OK);
    }

}

