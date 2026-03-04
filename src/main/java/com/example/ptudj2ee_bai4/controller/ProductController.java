package com.example.ptudj2ee_bai4.controller;

import com.example.ptudj2ee_bai4.model.Category;
import com.example.ptudj2ee_bai4.model.Product;
import com.example.ptudj2ee_bai4.service.CategoryService;
import com.example.ptudj2ee_bai4.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public String Index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    @GetMapping("/create")
    public String Create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    @PostMapping("/create")
    public String Create(@Valid Product newProduct, BindingResult result,
                         @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }
        productService.updateImage(newProduct, imageProduct); // Xử lý ảnh
        Integer categoryId = newProduct.getCategoryId();
        Category selectedCategory = categoryId != null ? categoryService.get(categoryId) : null;
        String categoryName = selectedCategory != null ? selectedCategory.getName() : null;
        newProduct.setCategoryName(categoryName);
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String Edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);
        if (find == null) {
            return "redirect:/products"; // Product not found, redirect to list
        }
        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    @GetMapping("/edit")
    public String EditFallback() {
        // Fallback: if user access /products/edit without ID, redirect to products list
        return "redirect:/products";
    }

    @PostMapping("/edit")
    public String Edit(@Valid Product editProduct,
                       BindingResult result,
                       @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
                       Model model) {
        try {
            System.out.println("=== EDIT POST ===");
            System.out.println("Product ID: " + editProduct.getId());
            System.out.println("Product Name: " + editProduct.getProductName());
            System.out.println("Category ID: " + editProduct.getCategoryId());
            System.out.println("Has errors: " + result.hasErrors());
            
            // Validate product ID exists
            if (editProduct.getId() <= 0) {
                System.out.println("Invalid product ID");
                return "redirect:/products";
            }
            
            // If validation failed, show form with errors
            if (result.hasErrors()) {
                System.out.println("VALIDATION ERRORS:");
                result.getAllErrors().forEach(e -> System.out.println("  - " + e.getDefaultMessage()));
                model.addAttribute("product", editProduct);
                model.addAttribute("categories", categoryService.getAll());
                return "product/edit";
            }
            
            // Process image upload if provided
            if (imageProduct != null && !imageProduct.isEmpty()) {
                try {
                    productService.updateImage(editProduct, imageProduct);
                } catch (Exception imgEx) {
                    System.out.println("Image upload error: " + imgEx.getMessage());
                    model.addAttribute("product", editProduct);
                    model.addAttribute("categories", categoryService.getAll());
                    model.addAttribute("errorMessage", "Lỗi khi tải ảnh: " + imgEx.getMessage());
                    return "product/edit";
                }
            }
            
            // Get and set category name
            Integer categoryId = editProduct.getCategoryId();
            if (categoryId != null && categoryId > 0) {
                Category selectedCategory = categoryService.get(categoryId);
                if (selectedCategory != null) {
                    editProduct.setCategoryName(selectedCategory.getName());
                }
            }
            
            System.out.println("Updating product with ID: " + editProduct.getId());
            productService.update(editProduct);
            System.out.println("Update successful, redirecting to /products");
            return "redirect:/products";
            
        } catch (Exception e) {
            System.out.println("FATAL ERROR in edit POST: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            return "product/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String Delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}