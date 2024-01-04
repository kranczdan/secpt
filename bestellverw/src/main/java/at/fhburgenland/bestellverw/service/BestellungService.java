package at.fhburgenland.bestellverw.service;

import at.fhburgenland.bestellverw.base.BaseMapper;
import at.fhburgenland.bestellverw.dto.BestellungDto;
import at.fhburgenland.bestellverw.dto.BestellungEventDto;
import at.fhburgenland.bestellverw.exception.NotFoundException;
import at.fhburgenland.bestellverw.model.Bestellung;
import at.fhburgenland.bestellverw.repository.BestellungRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class BestellungService extends BaseMapper {
    @Autowired
    BestellungRepository repository;

    @Autowired
    KafkaTemplate<String, BestellungEventDto> kafkaTemplate;

    public BestellungDto get(Long id) throws NotFoundException {
        Optional<Bestellung> optionalBestellung = repository.findById(id);

        if(!optionalBestellung.isPresent()){
            throw new NotFoundException("Sensor with the ID " + id + " not found");
        }

        return map(optionalBestellung.get(), BestellungDto.class);
    }

    public List<BestellungDto> read(){
        List<Bestellung> bestellungen = repository.findAll();
        return mapAll(bestellungen, BestellungDto.class);
    }

    public BestellungDto create(BestellungDto bestellungDto){
        Bestellung bestellung = map(bestellungDto, Bestellung.class);
        bestellung.setStatus("CREATED");
        try{
            bestellung = repository.save(bestellung);
            bestellungDto.setId(bestellung.getId());

            BestellungEventDto bestellungEventDto = new BestellungEventDto();
            bestellungEventDto.setBestellung(bestellungDto);
            bestellungEventDto.setType("ORDER CREATED");

            kafkaTemplate.send("new-order", bestellungEventDto);
        }catch(Exception e){
            bestellung.setStatus("FAILED");
            repository.save(bestellung);
        }

        return map(bestellung, BestellungDto.class);
    }

    @KafkaListener(topics = "complete-order", groupId = "orders-group")
    public void complete(String event){
        try{
            BestellungEventDto bestellungEventDto = new ObjectMapper().readValue(event, BestellungEventDto.class);
            Optional<Bestellung> entityOpt = repository.findById(bestellungEventDto.getBestellung().getId());
            if(entityOpt.isPresent()){

                //Test Payment Failed
                if(false){
                    bestellungEventDto.setType("PAYMENT FAILED");
                    kafkaTemplate.send("reverse-product", bestellungEventDto);
                }else{
                    entityOpt.get().setStatus("COMPLETED");
                    repository.save(entityOpt.get());
                }

            }
        } catch (Exception e){

        }
    }

    @KafkaListener(topics = "reverse-order", groupId = "orders-group")
    public void reverse(String event){
        try{
            BestellungEventDto bestellungEventDto = new ObjectMapper().readValue(event, BestellungEventDto.class);
            Optional<Bestellung> entityOpt = repository.findById(bestellungEventDto.getBestellung().getId());
            if(entityOpt.isPresent()){
                entityOpt.get().setStatus(bestellungEventDto.getType());
                repository.save(entityOpt.get());
            }
        } catch (Exception e){

        }
    }

    public BestellungDto update(Long id, BestellungDto customerDto){
        Optional<Bestellung> entityOpt = repository.findById(id);

        if (!entityOpt.isPresent()) {
            return null;
        }

        Bestellung bestellung = this.map(customerDto, Bestellung.class);
        bestellung.setId(id);
        return map(repository.save(bestellung), BestellungDto.class);
    }

    public void delete(Long id){
        Optional<Bestellung> entityOpt = repository.findById(id);

        if (entityOpt.isPresent()) {
            Bestellung bestellung = entityOpt.get();
            bestellung.setId(id);
            repository.delete(bestellung);
        }
    }
}
