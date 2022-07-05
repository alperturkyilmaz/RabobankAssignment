package nl.rabobank.mongo.repository;

import nl.rabobank.mongo.data.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<AccountEntity, String> {

    public Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
