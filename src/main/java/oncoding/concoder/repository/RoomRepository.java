package oncoding.concoder.repository;

import java.util.UUID;
import oncoding.concoder.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID>,
    QuerydslPredicateExecutor<Room> {

}
