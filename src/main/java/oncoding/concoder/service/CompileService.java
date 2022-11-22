package oncoding.concoder.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;
import oncoding.concoder.dto.CompileDto.Response;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompileService {
    public static final int THREAD_TIMEOUT_SECONDS = 5;

    public void writeFile(String name, String content) throws IOException {
        File file = new File(Paths.get(String.format("%s.py", name)).toString());

        FileWriter fw = new FileWriter(file);
        try (BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(content);
        }
    }

    /*
    파일 존재할 경우 : 삭제 (삭제 제대로 처리되지 않을 경우 print)
    파일 존재하지 않을 경우 : 무시
    */
    public void deleteFile(String name) {
        File file = new File(Paths.get(String.format("%s.py", name)).toString());

        if (!file.exists()) return;
        if (file.delete()) return;
        System.out.println("Error : file "+ Paths.get(String.format("file %s.py", name)).toString() +" not deleted!");
    }

    public CompileDto.Response getOutput(BufferedReader bufferedReader, int exitCode, long time) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        boolean first = true;
        while ((line = bufferedReader.readLine()) != null) {
            if (first) first = false;
            else sb.append("\n");
            sb.append(line);
        }
        bufferedReader.close();

        String result = exitCode!=0 ? "failed" : "success";
        log.info("run " + result + " with exit code " + exitCode + " time: " + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS)+"ms");
        return new CompileDto.Response(sb.toString(),  TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS));
    }

    public Timer setTimeoutTimer (Thread thread) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                thread.interrupt();
                log.info(thread.getName()+" thread timeout!");
            }
        };
        timer.schedule(timerTask, THREAD_TIMEOUT_SECONDS*1000);
        return timer;
    }

    @Async("taskExecutor")
    public Future<CompileDto.Response> run(String code, String input) throws IOException, InterruptedException {
        log.info(Thread.currentThread().getName()+" thread run()...");
        String random = UUID.randomUUID().toString();

        try {
            // set timer to timeout
           Timer timer = setTimeoutTimer(Thread.currentThread());

            // process start
            writeFile(random, code);
            ProcessBuilder processBuilder = new ProcessBuilder("python3",
                Paths.get(String.format("%s.py", random)).toString());
            long startTime = System.nanoTime();
            Process process = processBuilder.start();

            // write input
            OutputStream stdin = process.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stdin));
            bw.write(input);
            bw.close();

            // process end
            int exitCode = process.waitFor();
            long time = System.nanoTime()-startTime;
            timer.cancel();

            // read output
            InputStream stdout = exitCode!=0 ? process.getErrorStream() : process.getInputStream();
            BufferedReader br =  new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
            return new AsyncResult<>(getOutput(br, exitCode, time)); // TODO : 워크스페이스 Id 받아서 socket spread
        }
        finally {
            deleteFile(random);
        }
    }
}
