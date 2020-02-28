package com.example.garage.controllers;

import com.example.garage.model.Advert;
import com.example.garage.model.User;
import com.example.garage.repository.AdvertRepository;
import com.example.garage.repository.CarRepository;
import com.example.garage.services.UserService;
import com.example.garage.utilities.UserUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
public class AdvertController {

    @Autowired
    AdvertRepository advertRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    UserService userService;


    @RequestMapping(path = "/advertList")
    public String advertList(ModelMap model){
        List<Advert> ads = advertRepository.findAll();
        model.addAttribute("adverts", ads);
        return "advertList";
    }

    @GetMapping(value="advertCreate/{id}")
    public String advertCreate (@PathVariable Long id, ModelMap model) {
        Advert a = new Advert();
        a.setCar(carRepository.getOne(id));
        model.addAttribute("advert", a);
        return "advertCreate";
    }
    @PostMapping("/saveAdvert")
    public String saveAdvert(@ModelAttribute Advert form, ModelMap model) {
        String email = UserUtills.getLoggedUser();
        form.setOwner(userService.findUserByEmail(email));
        Date date = new Date(System.currentTimeMillis());
        form.setAdded(date);
        form.setActive(true);
        advertRepository.save(form);
        model.addAttribute("adverts", advertRepository.findAll());
        return "redirect:/advertList";
    }
    @GetMapping(value="advert/delete/{id}")
    public String deleteAdvert (@PathVariable Long id) {
        advertRepository.deleteById(id);
        return "redirect:/myAdverts";
    }
    @GetMapping(value="/advert/edit/{id}")
    public String advertEdit(ModelMap model, @PathVariable Long id) {
        Advert a = advertRepository.getOne(id);
        model.addAttribute("advert", a);
        return "advertEdit";
    }
    @PostMapping("/editAdvert")
    public String editAdvert(@ModelAttribute Advert form, ModelMap model) {
        String email = UserUtills.getLoggedUser();
        form.setOwner(userService.findUserByEmail(email));
        Date date = new Date(System.currentTimeMillis());
        form.setAdded(date);
        advertRepository.save(form);
        model.addAttribute("adverts", advertRepository.findAll());
        return "redirect:/myAdverts";
    }
    @GetMapping(value="/advert/details/{id}")
    public String advertDetails(ModelMap model, @PathVariable Long id) {
        Advert a = advertRepository.getOne(id);
        model.addAttribute("advert", a);
        return "advertDetails";
    }
    @RequestMapping(path = "/myAdverts")
    public String myAdverts(ModelMap model){
        String email = UserUtills.getLoggedUser();
        User user = userService.findUserByEmail(email);
        List<Advert> ads = advertRepository.findAll();
        model.addAttribute("adverts", ads);
        model.addAttribute("user", user);
        return "myAdverts";
    }
}
