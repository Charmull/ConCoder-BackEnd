package oncoding.concoder.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
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
    private Float averageTries;

    @Column
    private Integer timeLimit;

    @Column
    private Integer memoryLimit;

    @Column(columnDefinition = "MEDIUMTEXT")
    @NotNull
    private String description;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String input;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String output;

    @ManyToOne(fetch = FetchType.EAGER)
    private Level level;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "problem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProblemCategory> categories = new ArrayList<>();

    @Builder
    public Problem(Integer number, String title, Integer timeLimit, Integer memoryLimit, Float averageTries,
        String description, String input, String output,
        Level level, List<ProblemCategory> categories) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.input = input;
        this.output = output;
        this.averageTries = averageTries;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.level = level;
        this.categories = categories;
    }
}
