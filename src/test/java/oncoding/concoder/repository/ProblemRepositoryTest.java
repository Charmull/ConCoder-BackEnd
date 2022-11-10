package oncoding.concoder.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import oncoding.concoder.model.Level;
import oncoding.concoder.model.Problem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ProblemRepositoryTest {
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    LevelRepository levelRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void Problem_레벨별_랜덤_다건_조회() {
         // given
        int limit = 2;
        String levelName1 = "Silver 1";
        String levelName2 = "Gold 3";
        String title = "Problem_Title";
        String content = "Problem_Content";
        int number = 4242;
        Level level1 = levelRepository.save(new Level(levelName1));
        Level level2 = levelRepository.save(new Level(levelName2));

        List<Problem> problemList = List.of(
        Problem.builder().title(title).content(content).number(number++).level(level1).build(),
        Problem.builder().title(title).content(content).number(number++).level(level1).build(),
        Problem.builder().title(title).content(content).number(number++).level(level1).build(),
        Problem.builder().title(title).content(content).number(number++).level(level2).build()
        );
        problemRepository.saveAll(problemList);

        // when
        List<Problem> results = problemRepository.findRandomByLevel(level1.getId(), limit);

        // then
        assertThat(results.size()).isEqualTo(limit);
        results.forEach(problem ->
            assertThat(problem.getLevel().getId()).isEqualTo(level1.getId()));

    }

    @Test
    void Problem_카테고리별_랜덤_다건_조회() {
        // given

        // when

        // then

    }

    @Test
    void Problem_검색_단건_조회() {

    }

}
