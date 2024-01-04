package at.fhburgenland.bestellverw.controller;

import at.fhburgenland.bestellverw.dto.BestellungDto;
import at.fhburgenland.bestellverw.exception.NotFoundException;
import at.fhburgenland.bestellverw.service.BestellungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping({"/order"})
public class BestellungController {

    @Autowired
    BestellungService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity getBestellung(@PathVariable Long id){
        try{
            return ResponseEntity.ok(service.get(id));
        }catch (NotFoundException exception){
            return ResponseEntity.status(404).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getBestellungen(){
        return ResponseEntity.ok(service.read());
    }

    @PostMapping
    public ResponseEntity createBestellung(@RequestBody BestellungDto bestellungDto){
        BestellungDto createdBestellung = service.create(bestellungDto);
        return ResponseEntity.created(URI.create(createdBestellung.getId().toString())).body(createdBestellung);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateBestellung(@PathVariable Long id, @RequestBody BestellungDto bestellungDto){
        BestellungDto updatedBestellung = service.update(id, bestellungDto);
        if(updatedBestellung != null){
            return ResponseEntity.ok(updatedBestellung);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteBestellung(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
