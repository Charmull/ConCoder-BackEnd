package oncoding.concoder.repository;

import java.util.UUID;
import oncoding.concoder.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {

}
