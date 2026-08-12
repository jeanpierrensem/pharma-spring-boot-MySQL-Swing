package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import com.officine.losto.s7.stocks.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductPhotoStorageService productPhotoStorageService;
    private final MouvementStockRepository mouvementStockRepository;

    public ProductServiceImpl(
            ProductRepo productRepo,
            ProductPhotoStorageService productPhotoStorageService,
            MouvementStockRepository mouvementStockRepository) {
        this.productRepo = productRepo;
        this.productPhotoStorageService = productPhotoStorageService;
        this.mouvementStockRepository = mouvementStockRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> findAllByIdsInOrder(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Product> batch = productRepo.findAllById(ids);
        Map<Long, Product> byId = batch.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        List<Product> ordered = new ArrayList<>();
        for (Long id : ids) {
            Product p = byId.get(id);
            if (p != null) {
                ordered.add(p);
            }
        }
        return ordered;
    }

    @Override
    public Product loadById(long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product update(Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return productRepo.saveAll(products);
    }

    @Override
    public Product saveAndFlush(Product product) {
        return productRepo.saveAndFlush(product);
    }

    @Override
    public List<Product> saveAllAndFlush(List<Product> products) {
        return productRepo.saveAllAndFlush(products);
    }

    @Override
    public void remove(Product product) {
        if (product != null) {
            productPhotoStorageService.deleteStoredFileIfPresent(product);
        }
        productRepo.delete(product);
    }

    @Override
    public Product loadByCode(String code) {
        return productRepo.findByCodeBar(code);
    }

    @Override
    public List<Product> findByCriteria(String description, String name) {
        return productRepo.findByNameContainingOrCodeBarContaining(description, name);
    }

    @Override
    public List<Product> findByThresholdId(long thresholdId) {
        return productRepo.findDistinctByThresholdId(thresholdId);
    }

    @Override
    public List<Product> findBySectionId(long sectionId) {
        return productRepo.findBySection_Id(sectionId);
    }

    @Override
    public List<Product> findByBatchId(long batchId) {
        return mouvementStockRepository.findByBatch_Id(batchId).stream()
                .map(MouvementStock::getProduct)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public void incrementProductWarehouseQuantity(Sell sell) {
    }

    @Override
    public void decrementProductWarehouseQuantity(Sell sell) {
    }

}
