package oncoding.concoder.repository;

import java.util.UUID;
import oncoding.concoder.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
