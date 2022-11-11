package oncoding.concoder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Level extends JpaBaseEntity {
    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private Integer number;
}
