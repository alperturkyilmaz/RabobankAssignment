package nl.rabobank.api;

import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.PowerOfAttorney;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PowerOfAttorneyController {

    private final PowerOfAttorneyService powerOfAttorneyService;

    @PostMapping("/poa/")
    public PowerOfAttorney authorize(@RequestBody @Valid PowerOfAttorneyCreateDto powerOfAttorneyCreateDto) {
        return powerOfAttorneyService.authorize(powerOfAttorneyCreateDto);
    }

    @GetMapping("/poa/{granteeName}/accounts/")
    public List<Account> getAccountsGranted(@PathVariable String granteeName) {
        return powerOfAttorneyService.getAccountsGranted(granteeName);
    }

    @GetMapping("/poa/{granteeName}/")
    public List<PowerOfAttorney> getPowerOfAttorneys(@PathVariable String granteeName) {
        return powerOfAttorneyService.getPowerOfAttorneys(granteeName);
    }

}
