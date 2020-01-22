package com.example.garage;

import com.example.garage.model.Advert;
import com.example.garage.model.Car;
import com.example.garage.model.User;
import com.example.garage.repository.AdvertRepository;
import com.example.garage.repository.CarRepository;
import com.example.garage.repository.UserRepository;
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
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    AdvertRepository advertRepository;
    private Logged logged = new Logged();
    private static String UPLOADED_FOLDER = "C://Users//Admin//Desktop//garageMVC//src//main//resources//static//uploads//";

    @ModelAttribute("logged")
    public Logged getLogged(){
        return logged;
    }

    @RequestMapping(path = "/")
    public String initial(ModelMap model){
        model.addAttribute("logged",logged);
        return "index";
    }
    @RequestMapping(path = "/*")
    public String others(ModelMap model){
        model.addAttribute("logged",logged);
        return "index";
    }
    @RequestMapping(path="/logout")
    public String logout(ModelMap model){
        if(!logged.isLogged()) return "redirect:/";
        logged.setLogged(false);
        model.addAttribute("logged",logged);
        return "redirect:/";
    }
    //ONLY FOR ADMIN USE
    @RequestMapping(method = RequestMethod.GET, path="/userList")
    public String userList(ModelMap model)
    {
        if(logged.getLoggedUser().getId() != 2 ) return "redirect:/";
        List<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "userList";
    }
    @RequestMapping(path = "/login")
    public String login(ModelMap model){
        User u = new User();
        model.addAttribute("login", u);
        return "login";
    }
    @PostMapping(path="/loginRequest")
    public String loginRequest(@ModelAttribute User form){
        if(userRepository.existsByUsername(form.getUsername())){
            User u = userRepository.findByUsername(form.getUsername());
            if(u.getPassword().equals(form.getPassword())){
                logged.setLoggedUser(u);
                logged.setLogged(true);
                return "redirect:/index";
            }
            else return "redirect:/login";
        }
        else{
            return "redirect:/login";
        }

    }
    @RequestMapping("/register")
    public String addUser(ModelMap model) {
        User usr = new User();

        model.addAttribute("user", usr);
        return "register";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute User form, ModelMap model) {
        userRepository.save(form);
        model.addAttribute("users", userRepository.findAll());
        logged.setLoggedUser(userRepository.findByUsername(form.getUsername()));
        logged.setLogged(true);
        return "redirect:/index";
    }
    @RequestMapping(path="/carList")
    public String carList(ModelMap model){
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars",cars);
        if(!logged.isLogged()) return "redirect:/login";
        else return "carList";
    }
    @RequestMapping(path = "/advertList")
    public String advertList(ModelMap model){
        List<Advert> ads = advertRepository.findAll();
        model.addAttribute("adverts", ads);
        return "advertList";
    }
    @RequestMapping("/carCreate")
    public String carCreate(ModelMap model) {
        if(!logged.isLogged()) return "redirect:/login";
        else {
            Car c = new Car();

            model.addAttribute("car", c);
            return "carCreate";
        }
    }
    @PostMapping("/saveCar")
    public String saveCar(@RequestParam("file") MultipartFile file, @ModelAttribute Car form, ModelMap model) {
        form.setOwner(logged.getLoggedUser());
        if(file.isEmpty()){
            model.addAttribute("message", "Nie wybrano pliku");
            return "redirect:/carCreate";
        }
        form.setPicture(file.getOriginalFilename());
        carRepository.save(form);
        try {
            byte[] bytes = file.getBytes();
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
    @RequestMapping(value="car/delete/{id}", method = RequestMethod.GET)
    public String deleteUser (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if(!logged.isLogged()) return "redirect:/login";
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
        if(!logged.isLogged()) return "redirect:/login";
        else {
            Car c = carRepository.getOne(id);
            model.addAttribute("car", c);
            return "carEdit";
        }
    }
    @PostMapping("/editCar")
    public String editCar(@ModelAttribute Car form, ModelMap model) {
        if(!logged.isLogged()) return "redirect:/login";
        form.setOwner(logged.getLoggedUser());
        carRepository.save(form);

        model.addAttribute("cars", carRepository.findAll());
        return "redirect:/carList";
    }
    @RequestMapping(value="advertCreate/{id}", method = RequestMethod.GET)
    public String advertCreate (@PathVariable Long id, ModelMap model) {
        if(!logged.isLogged()) return "redirect:/login";
        else {
            Advert a = new Advert();
            a.setCar(carRepository.getOne(id));
            model.addAttribute("advert", a);
            return "advertCreate";
        }
    }
    @PostMapping("/saveAdvert")
    public String saveAdvert(@ModelAttribute Advert form, ModelMap model) {
        if(!logged.isLogged()) return "redirect:/login";
        form.setOwner(logged.getLoggedUser());
        Date date = new Date(System.currentTimeMillis());
        form.setAdded(date);
        form.setActive(true);
        advertRepository.save(form);
        model.addAttribute("adverts", advertRepository.findAll());
        return "redirect:/advertList";
    }
    @RequestMapping(value="advert/delete/{id}", method = RequestMethod.GET)
    public String deleteAdvert (@PathVariable Long id) {
        if(!logged.isLogged()) return "redirect:/login";
        advertRepository.deleteById(id);
        return "redirect:/myAdverts";
    }
    @RequestMapping(value="/advert/edit/{id}", method = RequestMethod.GET)
    public String advertEdit(ModelMap model, @PathVariable Long id) {
        if(!logged.isLogged()) return "redirect:/login";
        else {
            Advert a = advertRepository.getOne(id);
            model.addAttribute("advert", a);
            return "advertEdit";
        }
    }
    @PostMapping("/editAdvert")
    public String editAdvert(@ModelAttribute Advert form, ModelMap model) {
        if(!logged.isLogged()) return "redirect:/login";
        form.setOwner(logged.getLoggedUser());
        Date date = new Date(System.currentTimeMillis());
        form.setAdded(date);
        advertRepository.save(form);
        model.addAttribute("adverts", advertRepository.findAll());
        return "redirect:/myAdverts";
    }
    @RequestMapping(value="/advert/details/{id}", method = RequestMethod.GET)
    public String advertDetails(ModelMap model, @PathVariable Long id) {
            Advert a = advertRepository.getOne(id);
            model.addAttribute("advert", a);
            return "advertDetails";
    }
    @RequestMapping(path = "/myAdverts")
    public String myAdverts(ModelMap model){
        if(!logged.isLogged()) return "redirect:/login";
        List<Advert> ads = advertRepository.findAll();
        model.addAttribute("adverts", ads);
        return "myAdverts";
    }
}
