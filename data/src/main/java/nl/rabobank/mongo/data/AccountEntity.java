package nl.rabobank.mongo.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "account")
@Builder
public class AccountEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String accountNumber;
    private String accountHolderName;
    private Double balance;
    private AccountType accountType;
}
