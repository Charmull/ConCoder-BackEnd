package oncoding.concoder.repository;

import java.util.UUID;
import oncoding.concoder.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, UUID> {

}
