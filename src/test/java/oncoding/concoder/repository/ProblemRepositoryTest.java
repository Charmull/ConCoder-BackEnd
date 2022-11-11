package oncoding.concoder.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import oncoding.concoder.model.Category;
import oncoding.concoder.model.Level;
import oncoding.concoder.model.Problem;
import oncoding.concoder.model.ProblemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    @Autowired
    ProblemCategoryRepository problemCategoryRepository;

    @BeforeEach
    void initialize() {
        problemRepository.deleteAll();
        categoryRepository.deleteAll();
        levelRepository.deleteAll();
    }

    @Test
    void Problem_레벨별_랜덤_다건_조회() {
        // given
        int limit = 2;
        String levelName1 = "Silver 1";
        String levelName2 = "Gold 3";
        String title = "Problem_Title";
        String content = "Problem_Content";
        int number = 4242;
        Level level1 = levelRepository.save(new Level(levelName1, 234243));
        Level level2 = levelRepository.save(new Level(levelName2, 645646));

        List<Problem> problemList = List.of(
        Problem.builder().title(title).description(content).number(number++).level(level1).build(),
        Problem.builder().title(title).description(content).number(number++).level(level1).build(),
        Problem.builder().title(title).description(content).number(number++).level(level1).build(),
        Problem.builder().title(title).description(content).number(number++).level(level2).build()
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
        int limit = 3;
        String categoryName1 = "DP";
        String categoryName2 = "BFS";
        String title = "Problem_Title";
        String content = "Problem_Content";
        int number = 4242;
        Level level = levelRepository.save(new Level("Problem_Level", 234252));
        Category category1 = categoryRepository.save(new Category(categoryName1));
        Category category2 = categoryRepository.save(new Category(categoryName2));

        List<Problem> problemList = List.of(
            Problem.builder().title(title).description(content).number(number++).level(level).build(),
            Problem.builder().title(title).description(content).number(number++).level(level).build(),
            Problem.builder().title(title).description(content).number(number++).level(level).build(),
            Problem.builder().title(title).description(content).number(number++).level(level).build()
        );
        problemRepository.saveAll(problemList);
        List<ProblemCategory> problemCategoryList = List.of(
            new ProblemCategory(problemList.get(0), category1),
            new ProblemCategory(problemList.get(0), category2),
            new ProblemCategory(problemList.get(1), category1),
            new ProblemCategory(problemList.get(2), category1),
            new ProblemCategory(problemList.get(3), category1)
        );
        problemCategoryRepository.saveAll(problemCategoryList);

        // when
        List<Problem> result1 = problemRepository.findRandomByCategory(category1.getId(), limit);
        List<Problem> result2 = problemRepository.findRandomByCategory(category2.getId(), limit);

        // then
        assertThat(result1.size()).isEqualTo(limit);
        result1.forEach(problem -> {
            List<UUID> uuids = problem.getCategories().stream().map(
                problemCategory -> problemCategory.getCategory().getId()
            ).collect(Collectors.toList());
            assertThat(uuids).contains(category1.getId());
        });

        assertThat(result2.size()).isEqualTo(1);
        result2.forEach(problem -> {
            List<UUID> uuids = problem.getCategories().stream().map(
                problemCategory -> problemCategory.getCategory().getId()
            ).collect(Collectors.toList());
            assertThat(uuids).contains(category2.getId());
        });

    }

    @Test
    void Problem_검색_단건_조회() {
        // given
        int number = 2313;
        String title = "P_TITLE";
        String content = "P_CONTENT";
        Level level = levelRepository.save(new Level("P_LEVEL", 234234));
        Problem problem = Problem.builder()
            .title(title)
            .description(content)
            .number(number)
            .level(level)
            .build();
        problemRepository.save(problem);

        // when
        Optional<Problem> result = problemRepository.findFirstByNumber(number);

        // then
        if (result.isEmpty()) fail("해당하는 Problem이 존재하지 않습니다.");
        assertThat(result.get().getId()).isEqualTo(problem.getId());
        assertThat(result.get().getNumber()).isEqualTo(problem.getNumber());
        assertThat(result.get().getTitle()).isEqualTo(problem.getTitle());
        assertThat(result.get().getDescription()).isEqualTo(problem.getDescription());
    }

}
