package nl.rabobank;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.api.AccountCreateDto;
import nl.rabobank.api.PowerOfAttorneyCreateDto;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.data.AccountType;
import nl.rabobank.mongo.data.AuthorizationType;
import nl.rabobank.mongo.data.PowerOfAttorneyEntity;

public class TestUtils {

    public static PowerOfAttorneyEntity createPowerOfAttorneyEntity(int index) {
        return PowerOfAttorneyEntity.builder()
                .grantorName("Grantor-" + index)
                .granteeName("Grantee-" + index)
                .authorizationType(AuthorizationType.READ)
                .account(createAccountEntity(index))
                .build();
    }

    public static AccountEntity createAccountEntity(int index) {
        return AccountEntity.builder()
                .balance((double) (1000 * index))
                .accountType(AccountType.SAVINGS)
                .accountNumber("10000" + index)
                .accountHolderName("Grantor-" + index)
                .build();
    }


    public static AccountCreateDto createAccountCreateDto(int index) {
        return AccountCreateDto.builder()
                .accountType(AccountType.SAVINGS)
                .accountHolderName("AccountHolder" + index)
                .balance(100.00d * index)
                .build();
    }
    public static PowerOfAttorneyCreateDto createPowerOfAttorneyCreateDto(int index,String accountNumber) {

        return PowerOfAttorneyCreateDto.builder()
                .grantorName("Grantor-" + index)
                .granteeName("Grantee-" + index)
                .authorization(Authorization.READ)
                .accountNumber(accountNumber)
                .build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}