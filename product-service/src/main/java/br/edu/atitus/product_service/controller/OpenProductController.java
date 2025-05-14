package br.edu.atitus.product_service.controller;


import br.edu.atitus.product_service.entities.Product;
import br.edu.atitus.product_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class OpenProductController {

    private final ProductRepository repository;

    @Value("${server.port}")
    private String port;

    public OpenProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/{currency}")
    public Product findProduct(@PathVariable Long id, @PathVariable String currency) {
        Optional<Product> product = repository.findById(id);

        if (product.isPresent()) {
            Product p = product.get();
            p.setEnvironment("Product-Service PORT: " + port);
            p.setConvertedPrice(p.getPrice()); // simulação de conversão
            return p;
        }

        throw new RuntimeException("Product not found");
    }
}
