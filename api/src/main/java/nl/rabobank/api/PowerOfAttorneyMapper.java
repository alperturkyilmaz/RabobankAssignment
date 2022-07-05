package nl.rabobank.api;

import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.data.AuthorizationType;
import nl.rabobank.mongo.data.PowerOfAttorneyEntity;

import java.util.List;
import java.util.stream.Collectors;

public class PowerOfAttorneyMapper {
    private PowerOfAttorneyMapper() {

    }

    public static List<Account> toAccountsGranted(List<PowerOfAttorneyEntity> powerOfAttorneyEntityList) {
        return powerOfAttorneyEntityList.stream()
                .map(PowerOfAttorneyEntity::getAccount)
                .distinct()
                .map(AccountMapper::toAccount)
                .collect(Collectors.toList());
    }

    public static List<PowerOfAttorney> toPowerOfAttorney(List<PowerOfAttorneyEntity> powerOfAttorneyEntityList) {
        return powerOfAttorneyEntityList.stream()
                .map(PowerOfAttorneyMapper::toPowerOfAttorney)
                .collect(Collectors.toList());
    }

    public static PowerOfAttorney toPowerOfAttorney(PowerOfAttorneyEntity powerOfAttorneyEntity) {
        return PowerOfAttorney.builder()
                .account(AccountMapper.toAccount(powerOfAttorneyEntity.getAccount()))
                .authorization(convertToAuthorization(powerOfAttorneyEntity.getAuthorizationType()))
                .granteeName(powerOfAttorneyEntity.getGranteeName())
                .grantorName(powerOfAttorneyEntity.getGrantorName())
                .build();
    }

    public static PowerOfAttorneyEntity toPowerOfAttorneyEntity(PowerOfAttorneyCreateDto powerOfAttorneyCreateDto, AccountEntity accountEntity) {
        return PowerOfAttorneyEntity.builder().account(accountEntity)
                .authorizationType(convertToAuthorizationType(powerOfAttorneyCreateDto.getAuthorization()))
                .granteeName(powerOfAttorneyCreateDto.getGranteeName())
                .grantorName(powerOfAttorneyCreateDto.getGrantorName())
                .build();
    }

    private static AuthorizationType convertToAuthorizationType(Authorization authorization) {
        return authorization.equals(Authorization.READ) ? AuthorizationType.READ : AuthorizationType.WRITE;
    }

    private static Authorization convertToAuthorization(AuthorizationType authorizationType) {
        return authorizationType.equals(AuthorizationType.READ) ? Authorization.READ : Authorization.WRITE;
    }
}
