package sit.project.projectv1.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @NotBlank(message = "must not be blank")
    private String username;

    @NotBlank(message = "must not be blank")
    private String password;
}
