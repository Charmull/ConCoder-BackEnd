package oncoding.concoder.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.model.Level;
import oncoding.concoder.repository.LevelRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;

    public List<Level> getLevels() {
        return levelRepository.findAll();
    }

    public Map<Integer, Level> getLevelMapByNumbers(List<Integer> numbers) {
        List<Level> levels = levelRepository.findAllByNumberIn(numbers);
        return levels.stream()
            .collect(Collectors.toMap(Level::getNumber, Function.identity(), (older, newer)-> older));
    }
}
