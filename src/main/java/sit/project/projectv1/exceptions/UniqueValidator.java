package sit.project.projectv1.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import sit.project.projectv1.repositories.UserRepository;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private String fieldName;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        switch (fieldName) {
            case "username":
                return !userRepository.findUserName().contains(value);
            case "name":
                return !userRepository.findName().contains(value);
            case "email":
                return !userRepository.findEmail().contains(value);
        }
        return false;
    }
}
