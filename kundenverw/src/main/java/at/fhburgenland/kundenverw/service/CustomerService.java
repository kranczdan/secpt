package at.fhburgenland.kundenverw.service;

import at.fhburgenland.kundenverw.base.BaseMapper;
import at.fhburgenland.kundenverw.base.BaseMapper;
import at.fhburgenland.kundenverw.dto.CustomerDto;
import at.fhburgenland.kundenverw.exception.NotFoundException;
import at.fhburgenland.kundenverw.model.Customer;
import at.fhburgenland.kundenverw.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService extends BaseMapper {
    @Autowired
    CustomerRepository repository;

    public CustomerDto get(Long id) throws NotFoundException {
        Optional<Customer> optionalCustomer = repository.findById(id);

        if(!optionalCustomer.isPresent()){
            throw new NotFoundException("Sensor with the ID " + id + " not found");
        }

        return map(optionalCustomer.get(), CustomerDto.class);
    }

    public List<CustomerDto> read(){
        List<Customer> customers = repository.findAll();
        return mapAll(customers, CustomerDto.class);
    }

    public CustomerDto create(CustomerDto sensorDto){
        Customer customer = map(sensorDto, Customer.class);
        repository.save(customer);
        return map(customer, CustomerDto.class);
    }

    public CustomerDto update(Long id, CustomerDto customerDto){
        Optional<Customer> entityOpt = repository.findById(id);

        if (!entityOpt.isPresent()) {
            return null;
        }

        Customer customer = this.map(customerDto, Customer.class);
        customer.setId(id);
        return map(repository.save(customer), CustomerDto.class);
    }

    public void delete(Long id){
        Optional<Customer> entityOpt = repository.findById(id);

        if (entityOpt.isPresent()) {
            Customer customer = entityOpt.get();
            customer.setId(id);
            repository.delete(customer);
        }
    }
}
