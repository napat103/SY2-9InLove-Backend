package sit.project.projectv1.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.repositories.UserRepository;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private String fieldName;
    private Integer id;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;

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

        if (request.getMethod().equals("PUT")) {
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            Integer userId = Integer.parseInt(path.substring(11));
            User storedUser = userRepository.findById(userId).orElseThrow(() -> new ItemNotFoundException("This user does not exits!!!"));
            switch (fieldName) {
                case "username":
                    if (!storedUser.getUsername().equals(value) && userRepository.existsByUsername(value)) {
                        return false;
                    }
                    return true;
                case "name":
                    if (!storedUser.getName().equals(value) && userRepository.existsByName(value)) {
                        return false;
                    }
                    return true;
                case "email":
                    if (!storedUser.getEmail().equals(value) && userRepository.existsByEmail(value)) {
                        return false;
                    }
                    return true;
            }
        }

        // For create (POST)
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
