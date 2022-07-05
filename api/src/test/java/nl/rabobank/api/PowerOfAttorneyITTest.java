package nl.rabobank.api;

import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.TestUtils;
import nl.rabobank.account.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RaboAssignmentApplication.class)
@AutoConfigureMockMvc
class PowerOfAttorneyITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountService accountService;


    @Autowired
    PowerOfAttorneyService powerOfAttorneyService;

    @Test
    void testCreatePowerOfAttorneyEndpoint() throws Exception {
        Account account = accountService.createAccount(TestUtils.createAccountCreateDto(1));
        PowerOfAttorneyCreateDto powerOfAttorneyCreateDto = TestUtils.createPowerOfAttorneyCreateDto(1, account.getAccountNumber());
        powerOfAttorneyCreateDto.setGrantorName(account.getAccountHolderName());

        this.mockMvc.perform(post("/poa/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(powerOfAttorneyCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.accountNumber").value(account.getAccountNumber()));
    }

    @Test
    void testAccountsGrantedEndpoint() throws Exception {
        int listSize = 7;
        String granteeName = "Grantee-1";
        List<Account> accountList = IntStream.range(0, listSize)
                .mapToObj(i -> accountService.createAccount(TestUtils.createAccountCreateDto(i)))
                .collect(Collectors.toList());

        IntStream.range(0, listSize).forEach(i -> {
            PowerOfAttorneyCreateDto powerOfAttorneyCreateDto = TestUtils.createPowerOfAttorneyCreateDto(i, accountList.get(i).getAccountNumber());
            powerOfAttorneyCreateDto.setGranteeName(granteeName);
            powerOfAttorneyCreateDto.setGrantorName(accountList.get(i).getAccountHolderName());
            powerOfAttorneyService.authorize(powerOfAttorneyCreateDto);
        });

        this.mockMvc.perform(get("/poa/{granteeName}/accounts/", granteeName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(listSize)));
    }

}
