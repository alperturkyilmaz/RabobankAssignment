package nl.rabobank.mongo.repository;

import nl.rabobank.mongo.data.PowerOfAttorneyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PowerOfAttorneyRepository extends MongoRepository<PowerOfAttorneyEntity, String> {

    public List<PowerOfAttorneyEntity> findByGranteeName(String granteeName);
}
