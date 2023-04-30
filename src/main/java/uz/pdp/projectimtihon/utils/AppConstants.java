package uz.pdp.projectimtihon.utils;


import uz.pdp.projectimtihon.controller.AuthController;

import java.util.List;

public interface AppConstants {

    String[] OPEN_PAGES = {
            AuthController.BASE_PATH + "/**",
    };

    String AUTH_HEADER = "Authorization";
    String AUTH_TYPE_BEARER = "Bearer ";

    String BASE_PATH = "/api";

    List<String> EMAIL_LIST= List.of("pdp.uz" ,"mail.ru","epam.uz" ,"exede.uz");
}
