package br.edu.atitus.product_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.atitus.product_service.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
