package nl.rabobank.api;

import lombok.*;
import nl.rabobank.mongo.data.AccountType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountCreateDto {
    @NotNull
    AccountType accountType;

    @NotBlank
    String accountHolderName;

    Double balance;
}
