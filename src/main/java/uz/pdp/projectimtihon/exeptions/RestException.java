package uz.pdp.projectimtihon.exeptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private final String message;

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    private RestException(String message) {
        this.message = message;
    }

    private RestException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }


    public static RestException restThrow(String message) {
        return new RestException(message);
    }

    public static RestException restThrow(String message, HttpStatus status) {
        return new RestException(message, status);
    }
}
