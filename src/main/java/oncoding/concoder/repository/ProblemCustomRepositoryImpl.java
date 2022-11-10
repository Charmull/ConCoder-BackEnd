package oncoding.concoder.repository;

import static oncoding.concoder.model.QCategory.category;
import static oncoding.concoder.model.QLevel.level;

import com.querydsl.core.types.dsl.NumberExpression;
import java.util.List;
import java.util.UUID;
import oncoding.concoder.model.Problem;
import oncoding.concoder.model.QProblem;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProblemCustomRepositoryImpl extends QuerydslRepositorySupport implements  ProblemCustomRepository {
    public ProblemCustomRepositoryImpl() {
        super(Problem.class);
    }

    @Override
    public List<Problem> findRandomByLevel(UUID id, int limit) {
        final QProblem problem = QProblem.problem;

        return from(problem)
            .innerJoin(problem.level, level)
            .fetchJoin()
            .where(level.id.eq(id))
            .leftJoin(problem.categories)
            .fetchJoin()
            .orderBy(NumberExpression.random().asc())
            .limit(limit)
            .fetch();
    }

    @Override
    public List<Problem> findRandomByCategory(UUID id, int limit) {
        final  QProblem problem = QProblem.problem;

        return from(problem)
            .innerJoin(problem.categories)
            .fetchJoin()
            .where(category.id.eq(id))
            .orderBy(NumberExpression.random().asc())
            .limit(limit)
            .fetch();
    }


}