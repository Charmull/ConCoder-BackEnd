package oncoding.concoder.repository;

import java.util.Optional;
import oncoding.concoder.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>,
    QuerydslPredicateExecutor<Session> {

  Optional<Session> findBySessionId(final String sessionId);
}