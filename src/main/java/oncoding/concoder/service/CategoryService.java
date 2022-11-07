package oncoding.concoder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.model.Category;
import oncoding.concoder.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
