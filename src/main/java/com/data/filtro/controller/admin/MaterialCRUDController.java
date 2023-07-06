package com.data.filtro.controller.admin;

import com.data.filtro.model.Account;
import com.data.filtro.model.Material;
import com.data.filtro.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/material")
public class MaterialCRUDController {

    @Autowired
    MaterialService materialService;

    public Pageable sortFlavor(int currentPage, int pageSize, int sortType) {
        Pageable pageable;
        switch (sortType) {
            case 5, 10, 25, 50 -> pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id"));
            default -> {
                pageSize = 5;
                pageable = PageRequest.of(currentPage - 1, pageSize);
            }
        }
        return pageable;
    }

    @GetMapping()
    public String show(@RequestParam(defaultValue = "5") int sortType, @RequestParam("currentPage") Optional<Integer> page, Model model, HttpSession session) {
        Account admin = (Account) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        List<Material> activeMaterials = materialService.getActiveMaterial(1);
        int numberActiveMaterials = activeMaterials.size();
        int currentPage = page.orElse(1);
        int pageSize = sortType;
        Page<Material> materialPage;
        Pageable pageable;
        pageable = sortFlavor(currentPage, pageSize, sortType);
        materialPage = materialService.getAllPaging(pageable);
        model.addAttribute("materials", materialPage.getContent());
        model.addAttribute("totalPages", materialPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", materialPage.getTotalElements());
        model.addAttribute("sortType", sortType);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberActiveMaterials", numberActiveMaterials);
        return "admin/boot1/material";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Material material) {
        materialService.create(material);
        return "redirect:/admin/material";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Material material) {
        materialService.update(material);
        return "redirect:/admin/material";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        materialService.delete(id);
        return "redirect:/admin/material";
    }

}
