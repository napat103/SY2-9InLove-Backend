package sit.project.projectv1.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import sit.project.projectv1.models.User;
import sit.project.projectv1.repositories.UserRepository;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private String fieldName;

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

        // For update
        if (request.getMethod().equals("PUT")) {
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE); // "/api/users/userId"
            String[] pathSplit = path.split("/"); // [api, users, userId]
            Integer userId = Integer.parseInt(pathSplit[pathSplit.length-1]); // userId

            User storedUser = userRepository.findById(userId).orElseThrow(
                    () -> new ItemNotFoundException("This user does not exits!!!"));

            switch (fieldName) {
                case "username":
                    if (!storedUser.getUsername().equals(value) && userRepository.findUserName().contains(value)) {
                        return false;
                    }
                    return true;
                case "name":
                    if (!storedUser.getName().equals(value) && userRepository.findName().contains(value)) {
                        return false;
                    }
                    return true;
                case "email":
                    if (!storedUser.getEmail().equals(value) && userRepository.findEmail().contains(value)) {
                        return false;
                    }
                    return true;
            }
        }

        // For create
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
