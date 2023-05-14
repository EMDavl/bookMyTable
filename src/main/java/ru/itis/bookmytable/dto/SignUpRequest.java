package ru.itis.bookmytable.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotEmpty
    @Pattern(regexp = "^8(?:-\\d{3}){2}(?:-\\d{2}){2}$")
    private String phoneNumber;

    @Length(min = 8, max = 256)
    private String password;

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

}
