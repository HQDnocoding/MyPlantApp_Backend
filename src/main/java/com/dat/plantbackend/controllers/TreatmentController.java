package com.dat.plantbackend.controllers;


import com.dat.plantbackend.enities.Treatment;
import com.dat.plantbackend.services.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/treatments")
public class TreatmentController {

    @Autowired
    private TreatmentService treatmentService;

    @GetMapping
    public String listTreatments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam Map<String, String> filters,
            Model model
    ) {
        System.out.println("filters: " + filters);
        Page<Treatment> treatmentPage = treatmentService.getTreatments(filters, page, size);

        model.addAttribute("treatments", treatmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", treatmentPage.getTotalPages());
        model.addAttribute("params", filters); // giữ lại filter khi phân trang

        return "treatment";
    }

    @GetMapping("/create")
    public String createTreatment(Model model) {
        model.addAttribute(new Treatment());
        return "treatment_form";
    }

    @PostMapping
    public String createPesticide(Model model, @ModelAttribute("treatment") Treatment treatment) {

        try {
            System.out.printf(treatment.getInstruction());
            Treatment tr = this.treatmentService.saveTreatment(treatment);
            if (tr != null) {
                return "redirect:/treatments";
            }else{
                model.addAttribute("error", "Có lỗi xảy ra, không được để trống");

                return "treatment_form";

            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "treatment_form";
        }
    }

    @GetMapping("/{id}")
    public String showTreatment(@PathVariable("id") int id, Model model) {
        Treatment treatment = treatmentService.getTreatmentById(id);
        if (treatment == null) {
            return "redirect:/treatments";
        }
        model.addAttribute("treatment", treatment);
        return "treatment_detail";
    }

    @PostMapping("{id}")
    public String updateTreatment(@PathVariable("id") int id, @ModelAttribute("treatment") Treatment treatment) {
        try {
            treatment.setId(id);
            this.treatmentService.updateTreatment(treatment);
            return "redirect:/treatments";
        } catch (Exception e) {
            return "treatment_form";
        }
    }
}
