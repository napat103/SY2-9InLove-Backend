package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.project.projectv1.models.Category;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer categoryID) {
        return categoryRepository.findById(categoryID).orElseThrow(
                () -> new ItemNotFoundException("Not found this category"));
    }

    public Category createCategory(Category category) {
        return categoryRepository.saveAndFlush(category);
    }

    public Category updateCategory(Integer categoryId, Category category) {
        Category storedCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ItemNotFoundException("Not found this category"));
        storedCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.saveAndFlush(storedCategory);
    }

    public void deleteCategory(Integer categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new ItemNotFoundException("Not found this category");
        }
    }
}
