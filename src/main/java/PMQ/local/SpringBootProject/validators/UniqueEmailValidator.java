package PMQ.local.SpringBootProject.validators;

import org.springframework.beans.factory.annotation.Autowired;

import PMQ.local.SpringBootProject.annotations.UniqueEmail;
import PMQ.local.SpringBootProject.modules.users.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(email);
    }

}
