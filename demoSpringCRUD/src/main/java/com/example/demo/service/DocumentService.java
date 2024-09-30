package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Document;
import com.example.demo.entity.Magazine;
import com.example.demo.entity.Report;
import com.example.demo.repository.DocumentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;


@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final ObjectMapper objectMapper;

    public DocumentService(DocumentRepository documentRepository, ObjectMapper objectMapper) {
        this.documentRepository = documentRepository;
        this.objectMapper = objectMapper;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));
    }

    public Document saveDocument(@Valid JsonNode documentNode) {
        String type = documentNode.get("type").asText();
        if (type == null || type.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trường type không được để trống");
        }

        Document document;
        try {
            switch (type) {
                case "book":
                    document = objectMapper.treeToValue(documentNode, Book.class);
                    break;
                case "magazine":
                    document = objectMapper.treeToValue(documentNode, Magazine.class);
                    break;
                case "report":
                    document = objectMapper.treeToValue(documentNode, Report.class);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loại tài liệu không hợp lệ");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ: " + e.getMessage());
        }

        validateDocument(document, documentNode);

        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found, delete failed");
        }

        documentRepository.deleteById(id);
    }

    public List<Document> findDocumentsByType(String type) {
        List<Document> documents;

        switch (type.toLowerCase()) {
            case "book":
                documents = documentRepository.findByTypeBook(type);
                break;
            case "magazine":
                documents = documentRepository.findByTypeMagazine(type);
                break;
            case "report":
                documents = documentRepository.findByTypeReport(type);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type");
        }

        if (documents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents found of type: " + type);
        }

        return documents;
    }

    private void validateDocument(Document document, JsonNode documentNode) {
        if (!StringUtils.hasText(document.getNhaXuatBan())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhà xuất bản không được để trống");
        }

        String soBanXuatString = documentNode.get("soBanXuat").asText();
        int soBanXuat;
        try {
            soBanXuat = Integer.parseInt(soBanXuatString);
            if (soBanXuat <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số bản xuất phải là số nguyên dương");
        }
        document.setSoBanXuat(soBanXuat);

        if (document instanceof Book) {
            validateBook((Book) document);
        } else if (document instanceof Magazine) {
            validateMagazine((Magazine) document);
        } else if (document instanceof Report) {
            validateReport((Report) document);
        }
    }

    private void validateBook(Book book) {
        if (!StringUtils.hasText(book.getTacGia())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên tác giả không được để trống");
        }

        if (!book.getTacGia().matches("[^0-9]*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên tác giả không được chứa số");
        }

        if (book.getSoTrang() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số trang phải lớn hơn 0");
        }
    }

    private void validateMagazine(Magazine magazine) {
        if (!StringUtils.hasText(magazine.getBanPhatHanh())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bản phát hành không được để trống");
        }

        if (!StringUtils.hasText(magazine.getThangPhatHanh())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tháng phát hành không được để trống");
        }

        if (!magazine.getThangPhatHanh().matches("^(0[1-9]|1[0-2])/\\d{4}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tháng phát hành phải có định dạng mm/yyyy");
        }
    }

    private void validateReport(Report report) {
        if (report.getNgayPhatHanh() == null || !StringUtils.hasText(report.getNgayPhatHanh())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày phát hành không được để trống");
        }

        String dateString = report.getNgayPhatHanh();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            report.setNgayPhatHanh(date.toString());
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày phát hành phải có định dạng dd/MM/yyyy");
        }
    }
}
