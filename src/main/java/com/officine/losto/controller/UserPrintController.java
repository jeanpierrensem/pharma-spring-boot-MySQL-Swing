package com.officine.losto.controller;

import com.itextpdf.text.*;
import com.officine.losto.dto.*;
import com.officine.losto.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/print")
public class UserPrintController {

    private final UserPdfExportService userPdfExportService;

    public UserPrintController(UserPdfExportService userPdfExportService) {
        this.userPdfExportService = userPdfExportService;
    }

    /**
     * Builds a PDF user sheet (optionally with a Base64-encoded photo from the client).
     */
    @PostMapping(value = "/pdf", produces = "application/pdf", consumes = "application/json")
    public ResponseEntity<byte[]> printPdf(@RequestBody UserPrintRequestDto dto) throws DocumentException {
        byte[] pdf = userPdfExportService.buildUserSheetPdf(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("user-sheet.pdf").build());
        headers.setCacheControl(CacheControl.noStore());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
