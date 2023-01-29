package oncoding.concoder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CompileService.class})
@ActiveProfiles("test")
class CompileServiceTest {
    @Autowired
    private CompileService compileService;

    private final int THREAD_TIMEOUT_SECONDS = 10; // task timeout 설정 시간 (초)

    @Test
    void task_비동기_처리() {
        // given
        String text = "hello world";
        String pythonCode = "print(\""+text+"\", end=\"\")";

        // when
        String result = "";
        Future<String> future = null;
        try {
           future = compileService.run(pythonCode, "");
           result = future.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // then
        assertThat(result).isEqualTo(text);
        assertThat(future).isNotNull();
        assertThat(future).isDone();
        assertThat(future).succeedsWithin(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    void 특정_시간을_넘어가는_task_timeout_처리() {
        // given
        String timeoutCode = "while True:\n" + "    a = 1;";

        // when, then
        assertThatThrownBy(()-> compileService.run(timeoutCode, "").get())
                .isInstanceOf(InterruptedException.class);
    }

    @Test
    void 에러_발생하는_task_처리() {
        // given
        String errorCode = "print(";

        // when
        String result = "";
        Future<String> future = null;
        try {
            future = compileService.run(errorCode, "");
            result = future.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        // then
        assertThat(result).contains("Error"); // Error 내용 반환
        assertThat(future).isNotNull();
        assertThat(future).isDone(); // Task 자체는 시간 내 완료
        assertThat(future).succeedsWithin(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    void 인풋_받는_Task_실행() {
        // given
        String inputCode = "a=int(input())\n"
            +"print(a)\n"
            +"b=int(input())\n"
            +"print(b)";
        List<String> inputs = List.of("5\n15", "7\n21");
        List<String> expectedResults = List.of("5\n15", "7\n21");

        // when
        List<String> results = new ArrayList<>();
        List<Future<String>> futures = new ArrayList<>();
        try {
            for (int i = 0; i<inputs.size(); i++) {
                Future<String> future = compileService.run(inputCode, inputs.get(i));
                futures.add(future);
            }
            for (int i = 0; i<inputs.size(); i++) {
                results.add(futures.get(i).get());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        // then
        assertThat(results.size()).isEqualTo(inputs.size()); // Error 내용 반환
        for (int i = 0; i<inputs.size(); i++) {
            Future<String> future = futures.get(i);
            assertThat(future).isNotNull();
            assertThat(future).isDone(); // Task 자체는 시간 내 완료
            assertThat(future).succeedsWithin(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            String result = results.get(i);
            assertThat(result).isEqualTo(expectedResults.get(i));
        }

    }
}