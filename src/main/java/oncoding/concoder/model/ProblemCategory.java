package oncoding.concoder.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProblemCategory extends JpaBaseEntity {
    @ManyToOne
    private Problem problem;

    @ManyToOne
    private Category category;
}
