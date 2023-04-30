package uz.pdp.projectimtihon.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import uz.pdp.projectimtihon.utils.AppConstants;


@Getter
@RequiredArgsConstructor
@Builder
@ToString
public class TokenDTO {
    private final String refreshToken;

    private final String accessToken;

    private final String tokenType = AppConstants.AUTH_TYPE_BEARER;
}
