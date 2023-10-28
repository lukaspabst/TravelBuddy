package com.travelbuddy.demo.Entities;

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
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;

    @Indexed(unique = true)

    private String email;

    private String handy;

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
