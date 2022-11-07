package oncoding.concoder.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import oncoding.concoder.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    Optional<Problem> findFirstByNumber(int number);
    List<Problem> findByLevelId(UUID id);
}
