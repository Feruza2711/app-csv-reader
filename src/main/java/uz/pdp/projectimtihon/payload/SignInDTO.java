package uz.pdp.projectimtihon.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
