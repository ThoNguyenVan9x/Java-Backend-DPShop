package thonguyenvan.dpshop.services;


import thonguyenvan.dpshop.dtos.CategoryDTO;
import thonguyenvan.dpshop.models.Category;

import java.util.List;

public interface ICategoryService {

    Category createCategory(CategoryDTO category);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long categoryId, CategoryDTO category);

    void deleteCategory(long id);
}
