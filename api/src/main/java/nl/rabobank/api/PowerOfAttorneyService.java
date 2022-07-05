package nl.rabobank.api;

import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import nl.rabobank.api.exception.UnauthorizedException;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.data.AccountEntity;
import nl.rabobank.mongo.data.PowerOfAttorneyEntity;
import nl.rabobank.mongo.repository.AccountRepository;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PowerOfAttorneyService {
    private final PowerOfAttorneyRepository powerOfAttorneyRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public PowerOfAttorney authorize(@RequestBody PowerOfAttorneyCreateDto powerOfAttorneyCreateDto) {

        if (powerOfAttorneyCreateDto.getGranteeName().equals(powerOfAttorneyCreateDto.getGrantorName())) {
            throw new UnauthorizedException("Grantor can not give access to himself/herself");
        }

        AccountEntity accountEntity = accountRepository.findByAccountNumber(powerOfAttorneyCreateDto.getAccountNumber())
                .orElseThrow(() -> new NoSuchElementException("Account Not Found: " + powerOfAttorneyCreateDto.getAccountNumber()));

        if (!accountEntity.getAccountHolderName().equals(powerOfAttorneyCreateDto.getGrantorName())) {
            throw new UnauthorizedException("Grantor can not give access to the account: " + powerOfAttorneyCreateDto.getAccountNumber());
        }

        PowerOfAttorneyEntity powerOfAttorneyEntity = PowerOfAttorneyMapper.toPowerOfAttorneyEntity(powerOfAttorneyCreateDto, accountEntity);
        return PowerOfAttorneyMapper.toPowerOfAttorney(powerOfAttorneyRepository.save(powerOfAttorneyEntity));
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsGranted(String granteeName) {
        return PowerOfAttorneyMapper.toAccountsGranted(powerOfAttorneyRepository.findByGranteeName(granteeName));
    }
    @Transactional(readOnly = true)
    public List<PowerOfAttorney> getPowerOfAttorneys(String granteeName) {
        return PowerOfAttorneyMapper.toPowerOfAttorney(powerOfAttorneyRepository.findByGranteeName(granteeName));
    }
}
