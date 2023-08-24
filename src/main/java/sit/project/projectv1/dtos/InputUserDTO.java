package sit.project.projectv1.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputUserDTO {
    private String username;
    private String name;
    private String email;
    private String role;

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setRole(String role) {
        this.role = role.trim();
    }
}
