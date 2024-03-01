package thonguyenvan.dpshop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import thonguyenvan.dpshop.components.LocalizationUtils;
import thonguyenvan.dpshop.dtos.CategoryDTO;
import thonguyenvan.dpshop.models.Category;
import thonguyenvan.dpshop.services.ICategoryService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;


    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully");
    }




    @GetMapping("") //http:localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }



    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category with id = " + id + " successfully");
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id = " + id + " successfully");
    }
}
