package sit.project.projectv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.project.projectv1.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
