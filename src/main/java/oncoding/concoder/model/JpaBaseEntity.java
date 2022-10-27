package oncoding.concoder.model;

import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class JpaBaseEntity {
    @Id
    @GeneratedValue
    private UUID id;
}
