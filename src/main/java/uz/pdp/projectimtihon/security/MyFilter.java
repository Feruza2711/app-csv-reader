package uz.pdp.projectimtihon.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.projectimtihon.entity.User;
import uz.pdp.projectimtihon.repository.UserRepository;
import uz.pdp.projectimtihon.utils.AppConstants;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MyFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.token.access.key}")
    private String TOKEN_KEY;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AppConstants.AUTH_HEADER);

        try {
            if (Objects.nonNull(authHeader)) {
                   User user = getUserFromBearer(authHeader);
                UserPrincipal userPrincipal = new UserPrincipal(user);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                userPrincipal,
                                null,
                                userPrincipal.getAuthorities()
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private User getUserFromBearer(String authHeader) {
        Integer userId=null;
        try {
            userId = Integer.getInteger(Jwts
                    .parser()
                    .setSigningKey(TOKEN_KEY)
                    .parseClaimsJws(authHeader.substring(AppConstants.AUTH_TYPE_BEARER.length()))
                    .getBody()
                    .getSubject());
        } catch (ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException |
                SignatureException |
                 IllegalArgumentException e) {
           e.printStackTrace();
        }
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Bunday user yo'q"));
    }

}
