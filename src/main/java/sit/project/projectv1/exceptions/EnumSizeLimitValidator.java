package sit.project.projectv1.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumSizeLimitValidator implements ConstraintValidator<EnumSizeLimit, String> {
    private Set<String> allowedValues;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void initialize(EnumSizeLimit targetEnum) {
        Class<? extends Enum> enumSelected = targetEnum.targetClassType();
        allowedValues = (Set<String>) EnumSet.allOf(enumSelected).stream().map(e -> ((Enum<? extends Enum>) e).name())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return allowedValues.contains(value);
    }
}
