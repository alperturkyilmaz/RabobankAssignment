package nl.rabobank.api;

import nl.rabobank.TestUtils;
import nl.rabobank.api.exception.UnauthorizedException;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.data.PowerOfAttorneyEntity;
import nl.rabobank.mongo.repository.AccountRepository;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class PowerOfAttorneyTest {

    @Mock
    private PowerOfAttorneyRepository powerOfAttorneyRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PowerOfAttorneyService powerOfAttorneyService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGiveAuthorizationToTheGrantor() {
        PowerOfAttorneyCreateDto powerOfAttorneyCreateDto = PowerOfAttorneyCreateDto.builder()
                .grantorName("Grantor-1")
                .granteeName("Grantor-1")
                .authorization(Authorization.READ)
                .accountNumber(UUID.randomUUID().toString())
                .build();

        assertThatThrownBy(() -> powerOfAttorneyService.authorize(powerOfAttorneyCreateDto))
                .isInstanceOfAny(UnauthorizedException.class)
                .hasMessage("Grantor can not give access to himself/herself");
    }

    @Test
    void testGiveAuthorization() {

        AccountEntity accountEntity = TestUtils.createAccountEntity(1);
        PowerOfAttorneyCreateDto powerOfAttorneyCreateDto = TestUtils.createPowerOfAttorneyCreateDto(1, accountEntity.getAccountNumber());

        doReturn(Optional.of(accountEntity))
                .when(accountRepository).findByAccountNumber(powerOfAttorneyCreateDto.getAccountNumber());

        PowerOfAttorneyEntity powerOfAttorneyEntity = PowerOfAttorneyMapper.toPowerOfAttorneyEntity(powerOfAttorneyCreateDto, accountEntity);

        doReturn(powerOfAttorneyEntity).when(powerOfAttorneyRepository).save(any());

        PowerOfAttorney powerOfAttorney = powerOfAttorneyService.authorize(powerOfAttorneyCreateDto);

        assertThat(powerOfAttorney).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("account")
                .isEqualTo(powerOfAttorneyCreateDto);
    }

    @Test
    void testExceptionThrownWhenGrantorAndAccountHolderIsDifferent() {

        AccountEntity accountEntity = TestUtils.createAccountEntity(1);
        PowerOfAttorneyCreateDto powerOfAttorneyCreateDto = PowerOfAttorneyCreateDto.builder()
                .grantorName("DifferentGrantor-1")
                .granteeName("Grantee-1")
                .authorization(Authorization.READ)
                .accountNumber(accountEntity.getAccountNumber())
                .build();

        doReturn(Optional.of(accountEntity))
                .when(accountRepository).findByAccountNumber(powerOfAttorneyCreateDto.getAccountNumber());

        assertThatThrownBy(() -> powerOfAttorneyService.authorize(powerOfAttorneyCreateDto))
                .isInstanceOfAny(UnauthorizedException.class)
                .hasMessageContaining(powerOfAttorneyCreateDto.getAccountNumber());

    }


    @Test
    void testExceptionThrownWhenautorizationgivenTononExistingAccount() {

        AccountEntity accountEntity = TestUtils.createAccountEntity(1);
        PowerOfAttorneyCreateDto powerOfAttorneyCreateDto = PowerOfAttorneyCreateDto.builder()
                .grantorName("DifferentGrantor-1")
                .granteeName("Grantee-1")
                .authorization(Authorization.READ)
                .accountNumber(accountEntity.getAccountNumber())
                .build();

        doReturn(Optional.empty())
                .when(accountRepository).findByAccountNumber(powerOfAttorneyCreateDto.getAccountNumber());

        assertThatThrownBy(() -> powerOfAttorneyService.authorize(powerOfAttorneyCreateDto))
                .isInstanceOfAny(NoSuchElementException.class)
                .hasMessageContaining(powerOfAttorneyCreateDto.getAccountNumber())
                .hasMessageContaining("Account Not Found:");

    }
}
