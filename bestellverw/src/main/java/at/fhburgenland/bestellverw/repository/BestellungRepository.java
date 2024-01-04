package at.fhburgenland.bestellverw.repository;

import at.fhburgenland.bestellverw.model.Bestellung;
import org.springframework.data.repository.ListCrudRepository;

public interface BestellungRepository extends ListCrudRepository<Bestellung, Long>{
}
