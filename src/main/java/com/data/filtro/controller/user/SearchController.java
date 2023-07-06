package com.data.filtro.controller.user;

import com.data.filtro.model.Category;
import com.data.filtro.model.Material;
import com.data.filtro.model.Product;
import com.data.filtro.service.CategoryService;
import com.data.filtro.service.MaterialService;
import com.data.filtro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/search")
public class SearchController {


    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    MaterialService materialService;


    @ModelAttribute(name = "discountProducts")
    public List<Product> getDiscountProducts(Model model) {
        List<Product> productList = productService.getTopDiscountProducts();
        return productList;
    }

    @GetMapping("")
    public String showSearchPage(@RequestParam String name,
                                 @RequestParam(defaultValue = "best_selling") String sortType,
                                 @RequestParam(name = "page") Optional<Integer> page,
                                 Model model) {

        int currentPage = page.orElse(1);
        int pageSize = 6;
        Pageable pageable;
        Page<Product> productPage;

        switch (sortType) {
            case "product_name_asc":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("productName").ascending());
                break;
            case "product_name_desc":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("productName").descending());
                break;
            case "price_asc":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("price").ascending());
                break;
            case "price_desc":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("price").descending());
                break;
            case "newest":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("createdDate").ascending());
                break;
            case "oldest":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("createdDate").descending());
                break;
            default:
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("sold").descending());
                break;
        }

        productPage = productService.getProductsByName(name, pageable);
        List<Material> materialList = categoryService.getListMaterials();
        Category category = null;
        category = categoryService.getCategoryById(Integer.parseInt("1"));
        int currentId = 0;
        currentId = Integer.parseInt("1");
        String currentIdAll = "";
        currentIdAll = "all";
        int dataLowPrice = 0;
        int dataHighPrice = 1000;
        int dataMaterialId = 0;
        int dataCurrentPage = 1;

        model.addAttribute("materialList",materialList);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sortType", sortType);
        model.addAttribute("name", name);

        model.addAttribute("category", category);
        model.addAttribute("currentId", currentId);
        model.addAttribute("currentIdAll", currentIdAll);
        model.addAttribute("dataLowPrice", dataLowPrice);
        model.addAttribute("dataHighPrice", dataHighPrice);
        model.addAttribute("dataMaterialId", dataMaterialId);
        model.addAttribute("currentPage", dataCurrentPage);
        return "user/boot1/search";
    }

}
