package com.officine.losto.controller;

import com.itextpdf.text.*;
import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.entity.*;
import com.officine.losto.s7.stocks.service.*;
import com.officine.losto.service.*;
import com.officine.losto.validation.*;
import jakarta.validation.groups.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.*;
import java.time.*;
import java.util.List;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/api/products", produces = "application/json")
public class ProductController {
    private final ProductService productService;
    private final DtoMapper dtoMapper;
    private final ProductPdfExportService productPdfExportService;
    private final ProductCatalogEnrichmentService productCatalogEnrichmentService;

    public ProductController(
            ProductService productService,
            DtoMapper dtoMapper,
            ProductPdfExportService productPdfExportService,
            ProductCatalogEnrichmentService productCatalogEnrichmentService) {
        this.productService = productService;
        this.dtoMapper = dtoMapper;
        this.productPdfExportService = productPdfExportService;
        this.productCatalogEnrichmentService = productCatalogEnrichmentService;
    }

    @GetMapping
    public List<ProductResponseDto> getAll() {
        return productService.getAll().stream().map(this::toEnrichedResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable long id) {
        return toEnrichedResponse(productService.loadById(id));
    }

    @GetMapping("/by-code")
    public ProductResponseDto getByCode(@RequestParam String code) {
        return toEnrichedResponse(productService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<ProductResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String name) {
        return productService.findByCriteria(code, name).stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-threshold/{thresholdId}")
    public List<ProductResponseDto> listByThreshold(@PathVariable long thresholdId) {
        return productService.findByThresholdId(thresholdId).stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-section/{sectionId}")
    public List<ProductResponseDto> listBySection(@PathVariable long sectionId) {
        return productService.findBySectionId(sectionId).stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-batch/{batchId}")
    public List<ProductResponseDto> listByBatch(@PathVariable long batchId) {
        return productService.findByBatchId(batchId).stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ProductResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody ProductRequestDto dto) {
        return toEnrichedResponse(productService.save(dtoMapper.toProduct(dto)));
    }

    @PutMapping
    public ProductResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody ProductRequestDto dto) {
        return toEnrichedResponse(productService.update(dtoMapper.toProduct(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        productService.remove(productService.loadById(id));
    }

    /**
     * PDF export. If the body is omitted or {@code productIds} is null, all products are exported.
     * If {@code productIds} is empty, the PDF has no data rows (header only).
     */
    @PostMapping(value = "/export/pdf", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportProductsPdf(@RequestBody(required = false) ProductExportPdfRequest req)
            throws DocumentException {
        List<ProductResponseDto> dtos = resolveExportProducts(req);
        byte[] pdf = productPdfExportService.buildAllProductsListPdf(dtos);
        String filename = "officine-products-" + LocalDate.now() + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        headers.setCacheControl(CacheControl.noStore());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    private List<ProductResponseDto> resolveExportProducts(ProductExportPdfRequest req) {
        if (req == null || req.productIds() == null) {
            return productService.getAll().stream().map(this::toEnrichedResponse).toList();
        }
        if (req.productIds().isEmpty()) {
            return List.of();
        }
        return productService.findAllByIdsInOrder(req.productIds()).stream()
                .map(this::toEnrichedResponse)
                .toList();
    }

    private ProductResponseDto toEnrichedResponse(Product product) {
        ProductResponseDto dto = dtoMapper.toProductResponse(product);
        return productCatalogEnrichmentService.enrich(dto, product);
    }
}
