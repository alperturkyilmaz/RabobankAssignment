package nl.rabobank.api;

import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account/")
    public Account createAccount(@RequestBody @Valid AccountCreateDto account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/account/")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }
}
