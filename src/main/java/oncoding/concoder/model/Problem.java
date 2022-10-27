package oncoding.concoder.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends JpaBaseEntity {
    @Column(unique = true)
    @NotNull
    private Integer number;

    @Column
    @NotNull
    private String title;

    @Column
    private Float rate;

    @Column
    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private Level level;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "problem")
    private List<ProblemCategory> categories = new ArrayList<>();

}
