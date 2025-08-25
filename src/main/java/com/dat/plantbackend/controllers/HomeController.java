package com.dat.plantbackend.controllers;

import com.dat.plantbackend.repositories.PesticideRepository;
import com.dat.plantbackend.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    private final PesticideRepository pesticideRepository;

    public HomeController(UserRepository userRepository, PesticideRepository pesticideRepository) {
        this.userRepository = userRepository;
        this.pesticideRepository = pesticideRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Truyền dữ liệu ra view

        Long userCount = userRepository.count();
        Long pesticideCount = pesticideRepository.count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("pesticideCount", pesticideCount);
        model.addAttribute("title", "Trang quản lý");
        model.addAttribute("message", "Chào mừng bạn đến với hệ thống quản lý sầu riêng 🍃");

        return "index"; // Tên file index.html trong thư mục templates
    }
}
