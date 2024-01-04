package at.fhburgenland.product.service;

import at.fhburgenland.product.base.BaseMapper;
import at.fhburgenland.product.dto.BestellungEventDto;
import at.fhburgenland.product.dto.ProductDto;
import at.fhburgenland.product.exception.NotFoundException;
import at.fhburgenland.product.model.Product;
import at.fhburgenland.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService extends BaseMapper {
    @Autowired
    ProductRepository repository;

    @Autowired
    KafkaTemplate<String,BestellungEventDto> orderKafkaTemplate;

    public ProductDto get(Long id) throws NotFoundException {
        Optional<Product> optionalProduct = repository.findById(id);

        if(!optionalProduct.isPresent()){
            throw new NotFoundException("Sensor with the ID " + id + " not found");
        }

        return map(optionalProduct.get(), ProductDto.class);
    }

    public List<ProductDto> read(){
        List<Product> products = repository.findAll();
        return mapAll(products, ProductDto.class);
    }

    public ProductDto create(ProductDto sensorDto){
        Product product = map(sensorDto, Product.class);
        repository.save(product);
        return map(product, ProductDto.class);
    }

    public ProductDto update(Long id, ProductDto productDto){
        Optional<Product> entityOpt = repository.findById(id);

        if (!entityOpt.isPresent()) {
            return null;
        }

        Product product = this.map(productDto, Product.class);
        product.setId(id);
        return map(repository.save(product), ProductDto.class);
    }

    public void delete(Long id){
        Optional<Product> entityOpt = repository.findById(id);

        if (entityOpt.isPresent()) {
            Product product = entityOpt.get();
            product.setId(id);
            repository.delete(product);
        }
    }

    @KafkaListener(topics = "new-order", groupId = "orders-group")
    public void processAmount(String event){
        try{
        BestellungEventDto bestellungEventDto = new ObjectMapper().readValue(event, BestellungEventDto.class);
        Optional<Product> optionalProduct = repository.findById(bestellungEventDto.getBestellung().getProductId());

        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            if(product.getStock() >= bestellungEventDto.getBestellung().getAmount()){
                product.setStock(product.getStock()-bestellungEventDto.getBestellung().getAmount());
                repository.save(product);
                bestellungEventDto.setType("ORDER COMPLETED");
                orderKafkaTemplate.send("complete-order", bestellungEventDto);
            }else{
                BestellungEventDto reverseBestellungEventDto = new BestellungEventDto();
                reverseBestellungEventDto.setBestellung(bestellungEventDto.getBestellung());
                reverseBestellungEventDto.setType("PRODUCT OUT OF STOCK");
                orderKafkaTemplate.send("reverse-order", reverseBestellungEventDto);
            }
        }
        }catch (Exception e){
            System.out.println("Process Amount failed");
        }
    }

    @KafkaListener(topics = "reverse-product", groupId = "orders-group")
    public void reverse(String event){
        try{
            BestellungEventDto bestellungEventDto = new ObjectMapper().readValue(event, BestellungEventDto.class);

            Optional<Product> optionalProduct = repository.findById(bestellungEventDto.getBestellung().getProductId());

            if(optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setStock(product.getStock() + bestellungEventDto.getBestellung().getAmount());
                repository.save(product);
                orderKafkaTemplate.send("reverse-order", bestellungEventDto);
            }
        } catch (Exception e){

        }
    }
}
