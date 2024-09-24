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
    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    @Autowired
    private ObjectMapper objectMapper;

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
            Book book = (Book) document;


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


        if (document instanceof Magazine) {
            Magazine magazine = (Magazine) document;


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


        if (document instanceof Report) {
            Report report = (Report) document;

            if (report.getNgayPhatHanh() == null || !StringUtils.hasText(report.getNgayPhatHanh())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày phát hành không được để trống");
            }

            String dateString = report.getNgayPhatHanh();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            try {
                // Phân tích chuỗi thành LocalDate
                LocalDate date = LocalDate.parse(dateString, formatter);
                report.setNgayPhatHanh(date.toString());
            } catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày phát hành phải có định dạng dd/MM/yyyy");
            }
        }

        return documentRepository.save(document);
    }
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found, delete failed");
        }

        documentRepository.deleteById(id);
    }



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
