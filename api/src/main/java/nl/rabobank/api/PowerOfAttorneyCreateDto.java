package nl.rabobank.api;

import lombok.*;
import nl.rabobank.authorizations.Authorization;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PowerOfAttorneyCreateDto {
    @NotBlank
    String granteeName;

    @NotBlank
    String grantorName;

    @NotBlank
    String accountNumber;

    @NotNull
    Authorization authorization;
}
