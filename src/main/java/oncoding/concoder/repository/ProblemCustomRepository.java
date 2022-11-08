package oncoding.concoder.repository;

import java.util.List;
import java.util.UUID;
import oncoding.concoder.model.Problem;

public interface ProblemCustomRepository {
    List<Problem> findRandomByLevel(UUID id, int limit);
    List<Problem> findRandomByCategory(UUID id, int limit);
}