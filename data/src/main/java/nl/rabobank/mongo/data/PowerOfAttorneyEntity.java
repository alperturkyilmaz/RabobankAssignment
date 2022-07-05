package nl.rabobank.mongo.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "power_of_attorney")
@CompoundIndexes(value = {
        @CompoundIndex(unique = true, name = "grantee_grantor_account_auth", def = "{'granteeName' : 1, 'grantorName' : 1, 'account.id' : 1, 'authorizationType' : 1}")
})
@Builder
public class PowerOfAttorneyEntity {
    @Id
    private String id;


    String granteeName;


    String grantorName;

    @DBRef
    AccountEntity account;

    AuthorizationType authorizationType;
}
