package nl.rabobank.api;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.data.AccountType;

public class AccountMapper {
    private AccountMapper() {

    }

    public static Account toAccount(AccountEntity accountEntity) {
        return accountEntity.getAccountType().equals(AccountType.PAYMENT) ?
                new PaymentAccount(accountEntity.getAccountNumber(),
                        accountEntity.getAccountHolderName(),
                        accountEntity.getBalance())
                : new SavingsAccount(accountEntity.getAccountNumber(),
                accountEntity.getAccountHolderName(),
                accountEntity.getBalance());
    }

    public static AccountEntity toAccountEntity(AccountCreateDto accountCreateDto) {
        return AccountEntity.builder()
                .accountHolderName(accountCreateDto.getAccountHolderName())
                .accountType(accountCreateDto.getAccountType())
                .balance(accountCreateDto.getBalance()).build();
    }
}
