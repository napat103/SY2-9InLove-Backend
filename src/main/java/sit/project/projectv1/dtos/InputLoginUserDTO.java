package sit.project.projectv1.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputLoginUserDTO {
    @NotBlank(message = "must not be blank")
    private String username;

    @NotBlank(message = "must not be blank")
    private String password;
}
