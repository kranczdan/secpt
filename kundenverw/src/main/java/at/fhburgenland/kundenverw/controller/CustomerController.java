package at.fhburgenland.kundenverw.controller;

import at.fhburgenland.kundenverw.dto.CustomerDto;
import at.fhburgenland.kundenverw.exception.NotFoundException;
import at.fhburgenland.kundenverw.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping({"/customer"})
public class CustomerController {

    @Autowired
    CustomerService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity getCustomer(@PathVariable Long id){
        try{
            return ResponseEntity.ok(service.get(id));
        }catch (NotFoundException exception){
            return ResponseEntity.status(404).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getCustomers(){
        return ResponseEntity.ok(service.read());
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerDto customerDto){
        CustomerDto createdCustomer = service.create(customerDto);
        return ResponseEntity.created(URI.create(createdCustomer.getId().toString())).body(createdCustomer);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto){
        CustomerDto updatedCustomer = service.update(id, customerDto);
        if(updatedCustomer != null){
            return ResponseEntity.ok(updatedCustomer);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteCustomer(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
