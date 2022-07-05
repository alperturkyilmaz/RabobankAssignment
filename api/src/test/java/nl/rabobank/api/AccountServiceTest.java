package nl.rabobank.api;

import nl.rabobank.TestUtils;
import nl.rabobank.account.Account;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;


    @InjectMocks
    private AccountService accountService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAccountCreation() {
        AccountCreateDto createDto = TestUtils.createAccountCreateDto(1);
        AccountEntity accountEntity = AccountMapper.toAccountEntity(createDto);
        accountEntity.setId(UUID.randomUUID().toString());
        String accountNumber = UUID.randomUUID().toString();
        accountEntity.setAccountNumber(accountNumber);

        doReturn(accountEntity)
                .when(accountRepository).save(any());
        Account account = accountService.createAccount(createDto);
        assertThat(account).isInstanceOf(SavingsAccount.class);
        assertThat(account).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("accountNumber")
                .isEqualTo(createDto);
        assertThat(account).extracting("accountNumber").isEqualTo(accountNumber);
    }

    void testGetAccounts() {
        int listSize = 5;
        doReturn(IntStream.range(0, listSize)
                .mapToObj(TestUtils::createAccountEntity)
                .collect(Collectors.toList()))
                .when(accountRepository).findAll();

        List<Account> accounts = accountService.getAccounts();
        assertThat(accounts).hasSize(listSize);
        assertThat(accounts).
                usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isInstanceOf(SavingsAccount.class);
        assertThat(accounts).extracting("accountNumber").contains("100000", "100001", "100002", "100003", "100004");
    }
}
