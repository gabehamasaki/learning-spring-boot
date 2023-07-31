package com.gabriel.sales.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.sales.dtos.ProductRecordDto;
import com.gabriel.sales.models.ProductModel;
import com.gabriel.sales.repositories.ProductRepository;
import com.gabriel.sales.utils.Response;

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

  @GetMapping("/products")
  public ResponseEntity<List<ProductModel>> showAllProducts() {
    List<ProductModel> productsList = productRepository.findAll();
    
    if (!productsList.isEmpty()) {
      for (ProductModel product : productsList) {
        UUID id = product.getIdProduct();
        product.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).showProduct(id)).withSelfRel());
      }
    }

    return ResponseEntity.status(HttpStatus.OK).body(productsList); 
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Object> showProduct(@PathVariable UUID id) {
    Optional<ProductModel> product = productRepository.findById(id);

    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Product not found"));
    }

    return ResponseEntity.status(HttpStatus.OK).body(product.get());
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Object> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
    Optional<ProductModel> validateProduct = productRepository.findById(id);
        if (validateProduct.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Product not found"));
    }

    ProductModel product = validateProduct.get();
    BeanUtils.copyProperties(productRecordDto, product);

    productRepository.save(product);

    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable UUID id) {
    Optional<ProductModel> product = productRepository.findById(id);
    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Product not found"));
    }
    productRepository.delete(product.get());

    return ResponseEntity.status(HttpStatus.OK).body(new Response("Product with id " + id + " has been deleted"));
  }

}