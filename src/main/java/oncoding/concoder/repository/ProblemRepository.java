package oncoding.concoder.repository;

import java.util.Optional;
import java.util.UUID;
import oncoding.concoder.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, UUID>, ProblemCustomRepository {
    Optional<Problem> findFirstByNumber(int number);
    Optional<Problem> findTopByOrderByNumberDesc();
}
