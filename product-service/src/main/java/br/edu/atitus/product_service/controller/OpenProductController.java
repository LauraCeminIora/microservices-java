package br.edu.atitus.product_service.controller;


import br.edu.atitus.product_service.clients.CurrencyClient;
import br.edu.atitus.product_service.clients.CurrencyResponse;
import br.edu.atitus.product_service.entities.Product;
import br.edu.atitus.product_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class OpenProductController {

    private final ProductRepository repository;
    private final CurrencyClient currencyClient;

    @Value("${server.port}")
    private String port;

    public OpenProductController(ProductRepository repository, CurrencyClient currencyClient) {
    	super();
        this.repository = repository;
		this.currencyClient = currencyClient;
    }

    @GetMapping("/{id}/{currency}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id, @PathVariable String currency) {
        Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

            product.setEnvironment("Product-Service PORT: " + port);
            
        
        if (currency.equals(product.getCurrency()))
        	product.setConvertedPrice(product.getPrice());
        else {
        	CurrencyResponse currencyResponse = currencyClient.getCurrency(product.getPrice(), product.getCurrency(), currency);
        	product.setConvertedPrice(currencyResponse.getConvertedValue());
        	product.setEnvironment(product.getEnvironment() +  " - " + currencyResponse.getEnviroment());
        }

        return ResponseEntity.ok(product);
        
    }
}
