package at.fhburgenland.product.repository;

import at.fhburgenland.product.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product, Long>{
}
