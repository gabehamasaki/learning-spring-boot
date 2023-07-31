package  com.gabriel.sales.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.sales.models.ProductModel;


public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
} 