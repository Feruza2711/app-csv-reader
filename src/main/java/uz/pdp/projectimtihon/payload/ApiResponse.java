package uz.pdp.projectimtihon.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

//{success:true,
// message:"Muvaff"}
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse<E> {

    private final boolean success;

    private String message;

    private E data;

    private List<ErrorCode> errors;


    private ApiResponse() {
        this.success = true;
    }

    private ApiResponse(String message) {
        this.success = true;
        this.message = message;
    }

    private ApiResponse(E data) {
        this.success = true;
        this.data = data;
    }

    private ApiResponse(List<ErrorCode> errors) {
        this.success = false;
        this.errors = errors;
    }

    private ApiResponse(String message,E data) {
        this.success = false;
        this.message=message;
        this.data=data;
    }


    public static <T> ApiResponse<T> successResponse() {
        return new ApiResponse<>();
    }

    public static <T> ApiResponse<T> successResponse(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> successResponse(String message) {
        return new ApiResponse<>(message);
    }

    public static ApiResponse<List<ErrorCode>> failResponse(String message, int code) {
        return new ApiResponse<>(List.of(new ErrorCode(code, message)));
    }
    public static <T> ApiResponse<T> wrongResponse(String message,T data) {
        return new ApiResponse<>(message,data);
    }

}
