package com.data.filtro.controller.user;

import com.data.filtro.model.Category;
import com.data.filtro.model.Material;
import com.data.filtro.model.Product;
import com.data.filtro.service.CategoryService;
import com.data.filtro.service.MaterialService;
import com.data.filtro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    MaterialService materialService;


    @ModelAttribute(name = "discountProducts")
    public List<Product> getDiscountProducts(Model model) {
        List<Product> productList = productService.getTopDiscountProducts();
        return productList;
    }

    public Pageable sortPage(int currentPage, int pageSize, String sortType) {
        Pageable pageable;
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
        return pageable;
    }

    @GetMapping("/{id}")
    public String showProductsByCategory(@PathVariable String id,
                                         @RequestParam(defaultValue = "0") String lowPrice,
                                         @RequestParam(defaultValue = "1000") String highPrice,
                                         @RequestParam(defaultValue = "best_selling") String sortType,
                                         @RequestParam(defaultValue = "0") String materialId,
                                         @RequestParam(defaultValue = "1") String currentPage,
                                         Model model) {
        List<Material> materialList = categoryService.getListMaterials();
        int dataLowPrice = Integer.parseInt(lowPrice);
        int dataHighPrice = Integer.parseInt(highPrice);
        int dataMaterialId = Integer.parseInt(materialId);
        int dataCurrentPage = Integer.parseInt(currentPage);

        int pageSize = 6;
        int currentId = 0;
        Pageable pageable;
        Page<Product> productPage;
        Category category = null;
        String currentIdAll = "";

        pageable = sortPage(dataCurrentPage, pageSize, sortType);

        if (id.equals("all")) {
            if (dataHighPrice != 1000 && dataMaterialId != 0){
                productPage = productService.getProductByPriceAndMaterial(dataLowPrice, dataHighPrice, dataMaterialId, pageable);
            } else if (dataHighPrice != 1000){
                productPage = productService.getProductByPrice(dataLowPrice, dataHighPrice, pageable);
            } else if (dataMaterialId != 0){
                productPage = productService.getProductByMaterial(dataMaterialId, pageable);
            } else {
                productPage = productService.getAll(pageable);
            }
            currentIdAll = "all";
        } else {
            if (dataHighPrice != 1000 && dataMaterialId != 0){
                productPage = productService.getProductByCategoryAndPriceAndMaterial(Integer.parseInt(id), dataLowPrice, dataHighPrice, dataMaterialId, pageable);
            } else if (dataHighPrice != 1000){
                productPage = productService.getProductByCategoryAndPrice(Integer.parseInt(id), dataLowPrice, dataHighPrice, pageable);
            } else if (dataMaterialId != 0){
                productPage = productService.getProductByCategoryAndMaterial(Integer.parseInt(id), dataMaterialId, pageable);
            } else {
                productPage = productService.getProductByCategory(Integer.parseInt(id), pageable);
            }
            category = categoryService.getCategoryById(Integer.parseInt(id));
            currentId = Integer.parseInt(id);
        }
        List<Product> productListModel = productPage.getContent();
        model.addAttribute("materialList",materialList);
        model.addAttribute("products", productListModel);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sortType", sortType);
        model.addAttribute("category", category);
        model.addAttribute("currentId", currentId);
        model.addAttribute("currentIdAll", currentIdAll);
        model.addAttribute("dataLowPrice", dataLowPrice);
        model.addAttribute("dataHighPrice", dataHighPrice);
        model.addAttribute("dataMaterialId", dataMaterialId);
        model.addAttribute("currentPage", dataCurrentPage);
        return "user/boot1/shop";
    }


}
