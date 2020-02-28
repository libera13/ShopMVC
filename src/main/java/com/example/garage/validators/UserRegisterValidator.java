package com.example.garage.validators;

import com.example.garage.constants.Constants;
import com.example.garage.model.User;
import com.example.garage.utilities.AppUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserRegisterValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return User.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        User u = (User) obj;

//        Waliduje to co wklepał użytkownik, i wiąże błędy z message.properties
        ValidationUtils.rejectIfEmpty(errors, "username", "error.userUserName.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "error.userPassword.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.userEmail.empty");
        ValidationUtils.rejectIfEmpty(errors, "name", "error.userName.empty");
        ValidationUtils.rejectIfEmpty(errors, "last_name", "error.userLastName.empty");
        ValidationUtils.rejectIfEmpty(errors, "phone", "error.userPhone.empty");

        if (!u.getEmail().equals(null)) {
            boolean isMatch = AppUtils.checkEmailOrPassword(Constants.EMAIL_PATTERN, u.getEmail());
            if(!isMatch) {
                errors.rejectValue("email", "error.userEmailIsNotMatch");
            }
        }

        if (!u.getPassword().equals(null)) {
            boolean isMatch = AppUtils.checkEmailOrPassword(Constants.PASSWORD_PATTERN, u.getPassword());
            if(!isMatch) {
                errors.rejectValue("password", "error.userPasswordIsNotMatch");
            }
        }

    }

    public void validateEmailExist(User user, Errors errors) {
        if (user != null) {
            errors.rejectValue("email", "error.userEmailExist");
        }
    }
}
