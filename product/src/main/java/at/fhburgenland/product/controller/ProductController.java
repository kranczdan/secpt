package at.fhburgenland.product.controller;

import at.fhburgenland.product.dto.ProductDto;
import at.fhburgenland.product.exception.NotFoundException;
import at.fhburgenland.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping({"/product"})
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity getProduct(@PathVariable Long id){
        try{
            return ResponseEntity.ok(service.get(id));
        }catch (NotFoundException exception){
            return ResponseEntity.status(404).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getProducts(){
        System.out.println("Get Products");
        return ResponseEntity.ok(service.read());
    }

    @PostMapping
    public ResponseEntity createProduct(@RequestBody ProductDto productDto){
        ProductDto createdProduct = service.create(productDto);
        return ResponseEntity.created(URI.create(createdProduct.getId().toString())).body(createdProduct);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        ProductDto updatedProduct = service.update(id, productDto);
        if(updatedProduct != null){
            return ResponseEntity.ok(updatedProduct);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
