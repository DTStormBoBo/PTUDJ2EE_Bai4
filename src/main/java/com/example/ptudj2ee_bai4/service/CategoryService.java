package com.example.ptudj2ee_bai4.service;

import com.example.ptudj2ee_bai4.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private List<Category> listCategory = new ArrayList<>();

    public CategoryService() {
        // Khởi tạo sẵn một số danh mục mẫu để hiển thị trên giao diện
        listCategory.add(new Category(1, "Điện thoại"));
        listCategory.add(new Category(2, "Laptop"));
        listCategory.add(new Category(3, "Phụ kiện"));
    }

    public List<Category> getAll() {
        return listCategory;
    }

    public Category get(int id) {
        return listCategory.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(Category category) {
        int maxId = listCategory.stream().mapToInt(Category::getId).max().orElse(0);
        category.setId(maxId + 1);
        listCategory.add(category);
    }
}