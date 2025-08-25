package com.dat.plantbackend.controllers;


import com.dat.plantbackend.dto.CreateHistoryDTO;
import com.dat.plantbackend.dto.HistoryDTO;
import com.dat.plantbackend.dto.TreatmentDTO;
import com.dat.plantbackend.enities.History;
import com.dat.plantbackend.services.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiHistoryController {

    private final HistoryService historyService;

    @Value("${page.size}")
    private int pageSize;

    public ApiHistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/secure/histories")
    public ResponseEntity<?> getHistory(Principal principal, @RequestParam(defaultValue = "0") int page) {
        try {
            Page<HistoryDTO> historyList = this.historyService.getHistoryByUUID(UUID.fromString(principal.getName()), page, pageSize);
            return ResponseEntity.ok(historyList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/secure/histories")
    public ResponseEntity<?> createHistory(Principal principal, CreateHistoryDTO historyDTO) {
        try {

            System.out.println("uuid: " + principal.getName());

            if (this.historyService.saveHistory(historyDTO, UUID.fromString(principal.getName())) != null)
                return ResponseEntity.ok("Success");
            return ResponseEntity.badRequest().body("Failed to create history");

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/secure/histories")
    public ResponseEntity<?> deleteHistoryById(@RequestParam(name = "id") String id, Principal principal) {
        try {
            this.historyService.deleteHistoryById(UUID.fromString(id), UUID.fromString(principal.getName()));
            return ResponseEntity.ok("Success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
