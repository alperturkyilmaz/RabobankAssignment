package nl.rabobank.mapper;

import nl.rabobank.TestUtils;
import nl.rabobank.account.Account;
import nl.rabobank.api.AccountMapper;
import nl.rabobank.mongo.data.AccountEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    @Test
    void testAccountEntityToAccountMapping() {
        String accountNumber = UUID.randomUUID().toString();
        AccountEntity accountEntity = TestUtils.createAccountEntity(1);
        Account account = AccountMapper.toAccount(accountEntity);
        assertThat(account).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(accountEntity);

    }
}
