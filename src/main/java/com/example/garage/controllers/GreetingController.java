package com.example.garage.controllers;

import com.example.garage.model.User;
import com.example.garage.services.UserService;
import com.example.garage.utilities.UserUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class GreetingController {
	@Autowired
	UserService userService;

	@GetMapping(value = {"/index","/"})
	public String showIndex(){
//		String userName = UserUtills.getLoggedUser();
//		User user = userService.findUserByEmail(userName);
////
//		int nrRoli = user.getRoles().iterator().next().getId();
//
//		user.setNrRoli(nrRoli);
//
		return "index";
	}
}
