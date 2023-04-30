package uz.pdp.projectimtihon.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.projectimtihon.entity.User;
import uz.pdp.projectimtihon.exeptions.RestException;
import uz.pdp.projectimtihon.payload.*;
import uz.pdp.projectimtihon.repository.UserRepository;
import uz.pdp.projectimtihon.security.UserPrincipal;
import uz.pdp.projectimtihon.utils.AppConstants;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;


    @Value("${app.jwt.token.access.key}")
    private String ACCESS_TOKEN_KEY;

    @Value("${app.jwt.token.access.expiration}")
    private Long ACCESS_TOKEN_EXPIRATION;

    @Value("${app.jwt.token.refresh.key}")
    private String REFRESH_TOKEN_KEY;

    @Value("${app.jwt.token.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;


    public AuthService(UserRepository userRepository,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<TokenDTO> signIn(SignInDTO signInDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInDTO.getEmail(),
                            signInDTO.getPassword())
            );


            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Date accessExpirationDate = new Date(System.currentTimeMillis()
                    + ACCESS_TOKEN_EXPIRATION);

            String accessToken = Jwts
                    .builder()
                    .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_KEY)
                    .setSubject(userPrincipal.getId().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(accessExpirationDate)
                    .compact();

            Date refreshExpirationDate = new Date(System.currentTimeMillis()
                    + REFRESH_TOKEN_EXPIRATION);

            String refreshToken = Jwts
                    .builder()
                    .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_KEY)
                    .setSubject(userPrincipal.getId().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(refreshExpirationDate)
                    .compact();

            return ApiResponse.successResponse(
                    TokenDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build()
            );
        } catch (AuthenticationException e) {
            throw RestException.restThrow("Email or password wrong");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserPrincipal(user);
    }

    public ApiResponse<String> verificationEmail(String code) {
        User user = userRepository.findByVerificationCode(code).orElseThrow();
        if (user.isEnabled())
           throw RestException.restThrow("This user already active");

        user.setEnabled(true);
        userRepository.save(user);
        return ApiResponse.successResponse("OK");
    }

    public ApiResponse<String> signUp(SignUpDTO signUpDTO) {
        if (userRepository.existsByEmail(signUpDTO.getEmail()))
            return ApiResponse.wrongResponse(signUpDTO.getEmail(), "This email already exists");

        String responseEmail=emailCheck(signUpDTO.getEmail());
        if(!responseEmail.equals("ok")){
            return ApiResponse.wrongResponse(signUpDTO.getEmail(),responseEmail);
        }

        String responsePassword=passwordCheck(signUpDTO.getPassword());
        if(!responsePassword.equals("ok")){
            return ApiResponse.wrongResponse(signUpDTO.getPassword(),responsePassword);
        }

        if(!Objects.equals(signUpDTO.getPassword(),signUpDTO.getPrePassword())){
            return ApiResponse.wrongResponse(signUpDTO.getPrePassword(),"Passwords not match");
        }

        User user = new User(
                signUpDTO.getEmail(),
                passwordEncoder.encode(signUpDTO.getPassword())
        );
        user.setEnabled(false);
        UUID code = UUID.randomUUID();
        user.setVerificationCode(code.toString());
        userRepository.save(user);
       sendVerificationCodeToEmail(user);
        return ApiResponse.successResponse("We have sent click linc your email "+ user.getEmail()+".Please verify email.");
    }



    private void sendVerificationCodeToEmail(User user) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("");
        mailMessage.setText("YOUR VERIFICATION LINK: " + "http://localhost/api/auth/verification-email/" + user.getVerificationCode());

        javaMailSender.send(mailMessage);
    }


    public ApiResponse<String> resentCodeToEmail(String email) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw RestException.restThrow("User not found by this email");
        }
        User user = optionalUser.get();
        UUID code = UUID.randomUUID();
        user.setVerificationCode(code.toString());
        userRepository.save(user);

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("");
        mailMessage.setText("YOUR VERIFICATION LINK: " + "http://localhost/api/auth/verification-email/" + user.getVerificationCode());
        javaMailSender.send(mailMessage);
        return ApiResponse.successResponse("OK");
    }


    public ApiResponse<String> forgotPasswordEmailSentCode(String email) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw RestException.restThrow("User not found by this email");
        }
        User user = optionalUser.get();
        UUID code = UUID.randomUUID();
        user.setVerificationCodeForPassword(code.toString());
        userRepository.save(user);

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("");
        mailMessage.setText("YOUR VERIFICATION LINK: " + "http://localhost/api/auth/verification-email/forgot-password" + user.getVerificationCode());
        javaMailSender.send(mailMessage);
        return ApiResponse.successResponse("OK");
    }


    public ApiResponse<String> verificationEmailForPassword(String code) {
        Optional<User> optionalUser = userRepository.findByVerificationCodeForPassword(code);
        if(!optionalUser.isPresent()){
            throw RestException.restThrow("User not found");
        }
        User user = optionalUser.get();
        return ApiResponse.successResponse("OK");
    }

    public ApiResponse<String> changePassword(ChangePasswordDTO changePasswordDTO) {
        Optional<User> byEmail = userRepository.findByEmail(changePasswordDTO.getEmail());
        if(byEmail.isEmpty()){
            throw RestException.restThrow("User not found");
        }
        User user = byEmail.get();
        String responseCheckPassword = passwordCheck(changePasswordDTO.getPassword());
        if(!responseCheckPassword.equals("ok")){
            return ApiResponse.wrongResponse(changePasswordDTO.getPassword(),responseCheckPassword);
        }
        if(!changePasswordDTO.getPassword().equals(changePasswordDTO.getPrePassword())){
            return ApiResponse.wrongResponse(changePasswordDTO.getPrePassword(), "Passwords not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(user);
        return ApiResponse.successResponse("OK");
    }



    private String passwordCheck(String password) {
        return null;
    }

    private String emailCheck(String email){
        for (String e : AppConstants.EMAIL_LIST) {
            if(e.equals(email){
                return "This email is not available";
            }
        }
    }


}
