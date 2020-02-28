package com.example.garage.controllers;

import com.example.garage.model.User;
import com.example.garage.services.UserService;
import com.example.garage.validators.UserRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
public class RegisterController {

    //    @Autowired
//    ItemServiceImpl itemServiceImpl;
    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User u = new User();
        model.addAttribute("user", u);
        return "register";
    }

    @PostMapping("/register")
    public String sendRegisterForm(User user, BindingResult result, Model model, Locale locale) {

        String returnPage = null;

        User userExist = userService.findUserByEmail(user.getEmail());

        new UserRegisterValidator().validateEmailExist(userExist, result);

        new UserRegisterValidator().validate(user, result);

        if (result.hasErrors()) {
            returnPage = "register";
        } else {
//			user.setActivationCode(AppUtils.randomStringGenerator());
//
//			String content = "Wymagane potwierdzenie rejestracji. Kliknij w poniższy link aby aktywować konto: " +
//					"http://localhost:8080/activatelink/" + user.getActivationCode();

            userService.saveUser(user);
//			emailSender.sendEmail(user.getEmail(), "Potwierdzenie rejestracji", content);
//			model.addAttribute("message", messageSource.getMessage("user.register.success.email", null, locale));
//			//model.addAttribute("user", new User());
            returnPage = "index";

        }
        return returnPage;
    }
}