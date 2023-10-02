package sit.project.projectv1.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.exceptions.Password;
import sit.project.projectv1.exceptions.Unique;

@Getter
@Setter
public class InputCreateUserDTO {
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 45)
    @Unique(fieldName = "username")
    private String username;

    @Password
    private String password;

    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100)
    @Unique(fieldName = "name")
    private String name;

    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 150)
    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$")
    @Unique(fieldName = "email")
    private String email;

    private String role;


    public void setUsername(String username) {
        if (username != null) {
            this.username = username.trim();
        }
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name.trim();
        }
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.trim();
        }
    }

    public void setRole(String role) {
        if (role != null) {
            this.role = role.trim();
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = password.replaceAll("\\s", ""); // delete all space
        }
    }
}
