package sit.project.projectv1.exceptions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "{jakarta.validation.constraints.Pattern.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp();
}
