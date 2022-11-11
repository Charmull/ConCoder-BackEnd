package oncoding.concoder.repository;

import java.util.List;
import java.util.UUID;
import oncoding.concoder.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, UUID> {
    List<Level> findAllByNumberIn(List<Integer> numbers);
}
