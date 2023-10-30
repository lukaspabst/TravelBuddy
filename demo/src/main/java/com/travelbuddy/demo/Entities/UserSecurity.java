package com.travelbuddy.demo.Entities;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "usersSecurity")
public class UserSecurity {
    @Id
    @Schema(description = "Eindeutige ID des Benutzers", example = "12345", required = true)
    private String id;
    @Indexed(unique = true)
    @Schema(description = "Benutzername", example = "john_doe", required = true)
    private String username;
    @Schema(description  = "Passwort", example = "********", required = true)
    private String password;

    @Indexed(unique = true)
    @Schema(description = "E-Mail-Adresse des Benutzers", example = "john@example.com", required = true)
    private String email;

    @Schema(description  = "Handynummer des Benutzers", example = "+1234567890", required = true)
    private String handy;

    @Schema(description = "Gibt an, ob der Benutzer gesperrt ist", example = "false")
    private boolean notLocked;

    public UserSecurity(String id, String username, String password, String email, String handy, boolean notLocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.handy = handy;
        this.notLocked = notLocked;
    }

}
