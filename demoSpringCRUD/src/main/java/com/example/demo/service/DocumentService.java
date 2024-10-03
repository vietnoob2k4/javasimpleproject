package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Document;
import com.example.demo.entity.Magazine;
import com.example.demo.entity.Report;
import com.example.demo.repository.DocumentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    public void exportExcel(List<Document> documents, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Documents");

            Row headerRow = sheet.createRow(0);
            String[] columnsHeaders = {"ID", "Type", "NXB", "SBX", "SO TRANG", "TACGIA", "SO BAN PHAT HANH", "THANG PHAT HANH", "NGAY PHAT HANH"};
            for (int i = 0; i < columnsHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnsHeaders[i]);
            }

            int rowIndex = 1;
            for (Document document : documents) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(document.getId());
                row.createCell(1).setCellValue(document.getClass().getSimpleName());
                row.createCell(2).setCellValue(document.getNhaXuatBan());
                row.createCell(3).setCellValue(document.getSoBanXuat());

                if (document instanceof Book) {
                    Book book = (Book) document;
                    row.createCell(4).setCellValue(book.getSoTrang());
                    row.createCell(5).setCellValue(book.getTacGia());
                } else if (document instanceof Magazine) {
                    Magazine magazine = (Magazine) document;
                    row.createCell(6).setCellValue(magazine.getBanPhatHanh());
                    row.createCell(7).setCellValue(magazine.getThangPhatHanh());
                } else if (document instanceof Report) {
                    Report report = (Report) document;
                    row.createCell(8).setCellValue(report.getNgayPhatHanh());
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file Excel ");
        }
    }


    public void ImportExcel(MultipartFile file){
        List<Document> documents = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i<= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                String type = row.getCell(1).getStringCellValue();
                String nhaXuatBan = row.getCell(2).getStringCellValue();
                int soBanXuat = (int) row.getCell(3).getNumericCellValue();
                if("Book".equalsIgnoreCase(type)){
                    int soTrang = (int) row.getCell(4).getNumericCellValue();
                    String tacGia = row.getCell(5).getStringCellValue();
                    Book book = new Book();
                    book.setNhaXuatBan(nhaXuatBan);
                    book.setSoBanXuat(soBanXuat);
                    book.setSoTrang(soTrang);
                    book.setTacGia(tacGia);
                    documents.add(book);

                } else if ("Magazine".equalsIgnoreCase(type)){
                    String banPhatHanh = row.getCell(6).getStringCellValue();
                    String thangPhatHanh = row.getCell(7).getStringCellValue();
                    Magazine magazine = new Magazine();
                    magazine.setNhaXuatBan(nhaXuatBan);
                    magazine.setSoBanXuat(soBanXuat);
                    magazine.setBanPhatHanh(banPhatHanh);
                    magazine.setThangPhatHanh(thangPhatHanh);
                    documents.add(magazine);

                }else if ("Report".equalsIgnoreCase(type)) {
                    String ngayPhatHanh = row.getCell(8).getStringCellValue();
                    Report report = new Report();
                    report.setNhaXuatBan(nhaXuatBan);
                    report.setSoBanXuat(soBanXuat);
                    report.setNgayPhatHanh(ngayPhatHanh);
                    documents.add(report);
                }

            }
            documentRepository.saveAll(documents);

        }catch (IOException e){
            throw new RuntimeException("error import");
        }
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
