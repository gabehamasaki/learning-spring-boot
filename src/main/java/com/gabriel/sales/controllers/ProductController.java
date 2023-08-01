package com.gabriel.sales.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.sales.dtos.ProductRecordDto;
import com.gabriel.sales.models.ProductModel;
import com.gabriel.sales.repositories.ProductRepository;
import com.gabriel.sales.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products", produces = { "application/json" })
@Tag(name =  "Products Routes")
public class ProductController {
  @Autowired
  ProductRepository productRepository;

  @Operation(summary =  "Create a new product", method = "POST")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Successefully created a new product")
  })
  @PostMapping()
  public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
    var productModel = new ProductModel();
    BeanUtils.copyProperties(productRecordDto, productModel);

    ProductModel product = productRepository.save(productModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

  @Operation(summary =  "List all products", method = "GET")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "All products")
  })
  @GetMapping()
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

  @Operation(summary =  "List unique product by id", method = "GET")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Product successfully retrieved"),
    @ApiResponse(responseCode = "404", description = "Product not found"),
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> showProduct(@PathVariable UUID id) {
    Optional<ProductModel> product = productRepository.findById(id);

    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Product not found"));
    }

    return ResponseEntity.status(HttpStatus.OK).body(product.get());
  }

  @Operation(summary =  "Update product by id", method = "PUT")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Product successfully updated"),
    @ApiResponse(responseCode = "404", description = "Product not found"),
  })
  @PutMapping("/{id}")
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

  @Operation(summary =  "Delete product by id", method = "DELETE")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Product successfully deleted"),
    @ApiResponse(responseCode = "404", description = "Product not found"),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable UUID id) {
    Optional<ProductModel> product = productRepository.findById(id);
    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Product not found"));
    }
    productRepository.delete(product.get());

    return ResponseEntity.status(HttpStatus.OK).body(new Response("Product with id " + id + " has been deleted"));
  }

}