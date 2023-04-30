package uz.pdp.projectimtihon.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String prePassword;

}
