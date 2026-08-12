package com.officine.losto.controller;

import com.itextpdf.text.*;
import com.officine.losto.dto.*;
import com.officine.losto.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/print")
public class ProductPrintController {

    private final ProductPdfExportService productPdfExportService;

    public ProductPrintController(ProductPdfExportService productPdfExportService) {
        this.productPdfExportService = productPdfExportService;
    }

    @PostMapping(value = "/pdf", produces = "application/pdf", consumes = "application/json")
    public ResponseEntity<byte[]> printPdf(@RequestBody ProductPrintRequestDto dto) throws DocumentException {
        byte[] pdf = productPdfExportService.buildProductSheetPdf(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("product-sheet.pdf").build());
        headers.setCacheControl(CacheControl.noStore());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
