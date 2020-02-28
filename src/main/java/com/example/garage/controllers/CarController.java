package com.example.garage.controllers;

import com.example.garage.model.Advert;
import com.example.garage.model.Car;
import com.example.garage.model.User;
import com.example.garage.repository.AdvertRepository;
import com.example.garage.repository.CarRepository;
import com.example.garage.services.UserService;
import com.example.garage.utilities.UserUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@Controller
public class CarController {

    @Autowired
    UserService userService;

    @Autowired
    CarRepository carRepository;

    @Autowired
    AdvertRepository advertRepository;


    @RequestMapping(path="/carList")
    public String carList(ModelMap model){
        List<Car> cars = carRepository.findAll();
        String email = UserUtills.getLoggedUser();
        User user = userService.findUserByEmail(email);
        model.addAttribute("cars",cars);
        model.addAttribute("user", user);
        return "carList";
    }

    @RequestMapping("/carCreate")
    public String carCreate(ModelMap model) {
        Car c = new Car();

        model.addAttribute("car", c);
        return "carCreate";
    }

    @PostMapping("/saveCar")
    public String saveCar(@RequestParam("file") MultipartFile file, @ModelAttribute Car form, ModelMap model) {
        String email = UserUtills.getLoggedUser();
        form.setOwner(userService.findUserByEmail(email));
        if(file.isEmpty()){
            model.addAttribute("message", "Nie wybrano pliku");
            return "redirect:/carCreate";
        }
        form.setPicture(file.getOriginalFilename());
        carRepository.save(form);
        try {
            byte[] bytes = file.getBytes();

            String UPLOADED_FOLDER = "src//main//resources//static//uploads//";
            String folderPath = UPLOADED_FOLDER + form.getId() + "\\";

            Path pathToFolder = Paths.get(folderPath);
            if(!Files.exists(pathToFolder)){
                Files.createDirectories(pathToFolder);
            }
            Path path = Paths.get(folderPath + file.getOriginalFilename());
            System.out.println(path);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("cars", carRepository.findAll());
        return "redirect:/carList";
    }
    @GetMapping(value="car/delete/{id}")
    public String deleteUser (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Car c = carRepository.getOne(id);
        if(advertRepository.existsByCar(c)){
            redirectAttributes.addFlashAttribute("message", "Do tego samochodu istnieje jeszcze ogłoszenie. Usuń je najpierw.");
            return "redirect:/carList";
        }
        else {
            carRepository.deleteById(id);
            return "redirect:/carList";
        }
    }
    @RequestMapping(value="/carEdit/{id}", method = RequestMethod.GET)
    public String carEdit(ModelMap model, @PathVariable Long id) {
        Car c = carRepository.getOne(id);
        model.addAttribute("car", c);
        return "carEdit";
    }
    @PostMapping("/editCar")
    public String editCar(@ModelAttribute Car form, ModelMap model) {
        String email = UserUtills.getLoggedUser();
        form.setOwner(userService.findUserByEmail(email));
        carRepository.save(form);

        model.addAttribute("cars", carRepository.findAll());
        return "redirect:/carList";
    }

}
