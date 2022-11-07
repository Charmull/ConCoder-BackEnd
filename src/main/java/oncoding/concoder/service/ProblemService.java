package oncoding.concoder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.model.Problem;
import oncoding.concoder.model.ProblemCategory;
import oncoding.concoder.repository.ProblemCategoryRepository;
import oncoding.concoder.repository.ProblemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemCategoryRepository problemCategoryRepository;

    public Problem getProblemByNumber(int number) {
        return problemRepository
            .findFirstByNumber(number).get();
            // TODO : 해당하는 Problem 존재하지 않을 경우 예외 처리
    }

    public List<Problem> getProblemsByStandard(String standard, UUID id) {
        List<Problem> list = new ArrayList<>();
        if (standard.equals("level")) {
            list = problemRepository.findByLevelId(id);
        }
        else if (standard.equals("category")) {
            list =  problemCategoryRepository.findByCategoryId(id)
                .stream()
                .map(ProblemCategory::getProblem)
                .collect(Collectors.toList());
        }
        else {
            // TODO : 존재하지않는 standard 예외 처리
        }
        return list;
    }
}
