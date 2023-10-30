package com.travelbuddy.demo.Secruity.Infrastructure;


import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(description = "Benutzername", example = "john_doe", required = true)
    private String username;
    @Schema(description = "Passwort", example = "secret_password", required = true)
    private String password;
}
