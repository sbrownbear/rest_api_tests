package models.lombok;

import lombok.Data;

@Data
public class LoginBodyLombokModel {
    // "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"citislicka\" }"

    private String email, password;

}
