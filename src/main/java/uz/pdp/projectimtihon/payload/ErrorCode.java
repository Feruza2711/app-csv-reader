package uz.pdp.projectimtihon.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorCode {

    private int code;

    private String message;
}
