package uz.pdp.projectimtihon.exeptions;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.projectimtihon.payload.ApiResponse;
import uz.pdp.projectimtihon.payload.ErrorCode;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RestException.class)
    public HttpEntity<ApiResponse<List<ErrorCode>>> exceptionHandler(RestException ex) {
        return new ResponseEntity<>(
                ApiResponse.failResponse(
                        ex.getMessage(),
                        ex.getStatus().value()),
                ex.getStatus()
        );
    }
    @ExceptionHandler(AuthenticationException.class)
    public HttpEntity<ApiResponse<List<ErrorCode>>> exceptionHandler(AuthenticationException ex) {
        return new ResponseEntity<>(
                ApiResponse.failResponse(
                        ex.getMessage(),
                        HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED
        );
    }
}
