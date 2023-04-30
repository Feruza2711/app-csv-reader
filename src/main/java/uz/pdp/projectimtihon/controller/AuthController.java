package uz.pdp.projectimtihon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.projectimtihon.payload.*;
import uz.pdp.projectimtihon.service.AuthService;
import uz.pdp.projectimtihon.utils.AppConstants;

@RestController
@RequestMapping(AuthController.BASE_PATH)
@RequiredArgsConstructor
public class AuthController {
    public static final String BASE_PATH = AppConstants.BASE_PATH + "/auth";
    public static final String SIGN_UP_PATH = "sign-up";
    public static final String SIGN_IN_PATH = "sign-in";
    public static final String EMAIL_VERIFICATION_PATH = "email-verification/{code}";
    public static final String EMAIL_VERIFICATION_FOR_PASSWORD = "email-verification/forgot-password/{code}";
    private final AuthService authService;

    @PostMapping(SIGN_UP_PATH)
    public ApiResponse<String> signUp(@RequestBody @Valid SignUpDTO signUpDTO) {
        return authService.signUp(signUpDTO);
    }

    @PostMapping(SIGN_IN_PATH)
    public ApiResponse<TokenDTO> signIn(@RequestBody @Valid SignInDTO signInDTO) {
        return authService.signIn(signInDTO);
    }

    @GetMapping(EMAIL_VERIFICATION_PATH)
    public ApiResponse<String> verificationEmail(@PathVariable String code) {
        return authService.verificationEmail(code);
    }

    @PostMapping()
    public ApiResponse<String> resentVerificationLinkEmail(@RequestParam("email") String email){
      return authService.resentCodeToEmail(email);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPasswordEmailSentCode(@RequestParam("email") String email){
        return authService.forgotPasswordEmailSentCode(email);
    }

    @PostMapping(EMAIL_VERIFICATION_FOR_PASSWORD)
    public ApiResponse<String> verificationEmailForPassword(@PathVariable String code){
        return authService.verificationEmailForPassword(code);
    }

    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        return authService.changePassword(changePasswordDTO);
    }
}
