package oncoding.concoder.repository;

import static oncoding.concoder.model.QCategory.category;
import static oncoding.concoder.model.QLevel.level;
import static oncoding.concoder.model.QProblemCategory.problemCategory;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import java.util.List;
import java.util.UUID;
import oncoding.concoder.model.Problem;
import oncoding.concoder.model.QProblem;
import oncoding.concoder.model.QProblemCategory;
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
            .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
            .limit(limit)
            .fetch();
    }

    @Override
    public List<Problem> findRandomByCategory(UUID id, int limit) {
        final  QProblem problem = QProblem.problem;

        return from(problem)
            .innerJoin(problem.categories, problemCategory)
            .fetchJoin()
            .where(problemCategory.category.id.eq(id))
            .leftJoin(problem.level, level)
            .fetchJoin()
            .innerJoin(problemCategory.category, category)
            .fetchJoin()
            .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
            .limit(limit)
            .fetch();
    }


}