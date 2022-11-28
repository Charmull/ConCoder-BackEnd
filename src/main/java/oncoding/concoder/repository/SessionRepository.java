package oncoding.concoder.repository;

import java.util.Optional;
import java.util.UUID;
import oncoding.concoder.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID>,
    QuerydslPredicateExecutor<Session> {

  Optional<Session> findBySessionId(final String sessionId);
}