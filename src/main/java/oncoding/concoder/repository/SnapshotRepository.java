package oncoding.concoder.repository;

import java.util.UUID;
import oncoding.concoder.model.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SnapshotRepository extends JpaRepository<Snapshot, UUID>,
    QuerydslPredicateExecutor<Snapshot> {

}
