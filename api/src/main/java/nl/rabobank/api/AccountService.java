package nl.rabobank.api;

import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(AccountCreateDto accountCreateDto) {
        AccountEntity accountEntity = AccountMapper.toAccountEntity(accountCreateDto);
        accountEntity.setAccountNumber(UUID.randomUUID().toString());
        return AccountMapper.toAccount(accountRepository.save(accountEntity));
    }

    public List<Account> getAccounts() {
        List<AccountEntity> accountEntityList = accountRepository.findAll();
        return accountEntityList.stream().map(AccountMapper::toAccount).collect(Collectors.toList());
    }
}
