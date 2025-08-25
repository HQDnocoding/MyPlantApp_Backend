package com.dat.plantbackend.controllers;


import com.dat.plantbackend.enities.Pesticide;
import com.dat.plantbackend.services.DiseaseService;
import com.dat.plantbackend.services.PesticideService;
import com.dat.plantbackend.services.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pesticides")
public class PesticideController {

    @Autowired
    private PesticideService pesticideService;
    @Autowired
    private PlantService plantService;

    @Autowired
    private DiseaseService diseaseService;


    @GetMapping
    public String listPesticides(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            Model model
    ) {
        // Lấy dữ liệu phân trang
        Page<Pesticide> pesticidePage = pesticideService.getPesticides(page, size, name, description);

        // Lưu filter params để gán lại vào form tìm kiếm
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);

        // Truyền dữ liệu sang giao diện
        model.addAttribute("pesticides", pesticidePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pesticidePage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("params", params);

        return "pesticide"; // file HTML
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        return "pesticide_form";
    }

    @PostMapping
    public String createPesticide(Model model, @ModelAttribute("pesticide") Pesticide pesticide) {
        try {
            this.pesticideService.cretePesticide(pesticide);
            return "redirect:/pesticides";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pesticide_form";
        }
    }

}
