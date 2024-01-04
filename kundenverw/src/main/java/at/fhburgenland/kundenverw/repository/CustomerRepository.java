package at.fhburgenland.kundenverw.repository;

import at.fhburgenland.kundenverw.model.Customer;
import org.springframework.data.repository.ListCrudRepository;

public interface CustomerRepository extends ListCrudRepository<Customer, Long>{
}
