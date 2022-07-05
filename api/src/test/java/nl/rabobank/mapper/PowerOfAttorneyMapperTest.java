package nl.rabobank.mapper;

import nl.rabobank.TestUtils;
import nl.rabobank.account.Account;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.api.PowerOfAttorneyMapper;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.data.AccountType;
import nl.rabobank.mongo.data.AuthorizationType;
import nl.rabobank.mongo.data.PowerOfAttorneyEntity;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class PowerOfAttorneyMapperTest {

    @Test
    void testPowerOfAttorneyEntityToPowerOfAttorneyMapping() {
        String accountNumber = UUID.randomUUID().toString();
        PowerOfAttorneyEntity powerOfAttorneyEntity = PowerOfAttorneyEntity.builder()
                .grantorName("Grantor-1")
                .granteeName("Grantee-1")
                .authorizationType(AuthorizationType.READ)
                .account(AccountEntity.builder()
                        .balance(100.10)
                        .accountType(AccountType.SAVINGS)
                        .accountNumber(accountNumber)
                        .accountHolderName("Grantor-1")
                        .build())
                .build();
        PowerOfAttorney powerOfAttorney = PowerOfAttorneyMapper.toPowerOfAttorney(powerOfAttorneyEntity);
        assertThat(powerOfAttorney).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("authorization")
                .isEqualTo(powerOfAttorneyEntity);
        assertThat(powerOfAttorney).extracting("account").isExactlyInstanceOf(SavingsAccount.class);
    }


    @Test
    void testAccountsGrantedMapping() {
        int listSize = 5;
        List<PowerOfAttorneyEntity> powerOfAttorneyEntityList = IntStream.range(1, listSize)
                .mapToObj(TestUtils::createPowerOfAttorneyEntity)
                .collect(Collectors.toList());
        List<Account> accountsGranted = PowerOfAttorneyMapper.toAccountsGranted(powerOfAttorneyEntityList);
        assertThat(accountsGranted).hasSize(listSize - 1);
        assertThat(accountsGranted).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(powerOfAttorneyEntityList.stream()
                        .map(PowerOfAttorneyEntity::getAccount)
                        .collect(Collectors.toList()));
    }

    @Test
    void testTooPowerOfAttorneyMapping() {
        int listSize = 5;
        List<PowerOfAttorneyEntity> powerOfAttorneyEntityList = IntStream.range(1, listSize)
                .mapToObj(TestUtils::createPowerOfAttorneyEntity)
                .collect(Collectors.toList());
        List<PowerOfAttorney> powerOfAttorneyList = PowerOfAttorneyMapper.toPowerOfAttorney(powerOfAttorneyEntityList);
        assertThat(powerOfAttorneyList).hasSize(listSize - 1);
        assertThat(powerOfAttorneyList).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("authorization")
                .isEqualTo(powerOfAttorneyEntityList);
    }

}
