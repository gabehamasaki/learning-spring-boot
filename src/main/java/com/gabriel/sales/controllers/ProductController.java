package com.gabriel.sales.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.sales.dtos.ProductRecordDto;
import com.gabriel.sales.models.ProductModel;
import com.gabriel.sales.repositories.ProductRepository;

import jakarta.validation.Valid;

@RestController
public class ProductController {
  @Autowired
  ProductRepository productRepository;

  @PostMapping("/products")
  public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
    var productModel = new ProductModel();
    BeanUtils.copyProperties(productRecordDto, productModel);

    ProductModel product = productRepository.save(productModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

}