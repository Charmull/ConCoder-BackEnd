package oncoding.concoder.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.ProblemDto;
import oncoding.concoder.model.Level;
import oncoding.concoder.model.Problem;
import oncoding.concoder.model.ProblemCategory;
import oncoding.concoder.repository.ProblemCategoryRepository;
import oncoding.concoder.repository.ProblemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final LevelService levelService;
    private final CrawlingService crawlingService;

    public Problem getProblemByNumber(int number) {
        return problemRepository
            .findFirstByNumber(number).get();
            // TODO : 해당하는 Problem 존재하지 않을 경우 예외 처리
    }

    public List<Problem> getProblemsByStandard(String standard, UUID id) {
        List<Problem> list = new ArrayList<>();
        if (standard.equals("level")) {
            list = problemRepository.findRandomByLevel(id, 5);
        }
        else if (standard.equals("category")) {
            list =  problemRepository.findRandomByCategory(id, 5);
        }
        else {
            // TODO : 존재하지않는 standard 예외 처리
        }
        return list;
    }

    @Scheduled(cron = "0 0/20 * * * *")
    public void createProblems() throws IOException {
        Optional<Problem> lastNumberProblem = problemRepository.findTopByOrderByNumberDesc();

        int startNumber = lastNumberProblem.map(value -> value.getNumber() + 1).orElse(1000);
        List<ProblemDto.CreateRequest> rawProblems  = crawlingService.getRawProblems(startNumber);

        List<Integer> rawLevels = rawProblems.stream()
            .map(ProblemDto.CreateRequest::getLevel)
            .collect(Collectors.toList());
        Map<Integer, Level> levelMap = levelService.getLevelMapByNumbers(rawLevels);

        List<Problem> problems = new ArrayList<>();
        //TODO: 카테고리 생성 및 연관 처리
        for (ProblemDto.CreateRequest rawProblem : rawProblems) {
            Map<String, String> content = crawlingService.getContent(rawProblem.getProblemId());
            Problem problem = Problem.builder()
                .number(rawProblem.getProblemId())
                .title(rawProblem.getTitleKo())
                .description(content.get("description"))
                .input(content.get("input"))
                .output(content.get("output"))
                .level(levelMap.get(rawProblem.getLevel()))
                .averageTries(rawProblem.getAverageTries())
                .timeLimit(Integer.parseInt(content.get("timeLimit").replaceAll("[^\\d]*", "")))
                .memoryLimit(Integer.parseInt(content.get("memoryLimit").replaceAll("[^\\d]*", "")))
                .build();
            problems.add(problem);
        }
        problemRepository.saveAll(problems);
    }
}
