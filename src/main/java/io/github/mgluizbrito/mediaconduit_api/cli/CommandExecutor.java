package io.github.mgluizbrito.mediaconduit_api.cli;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractRequestDTO;
import io.github.mgluizbrito.mediaconduit_api.model.ExtractionTask;
import io.github.mgluizbrito.mediaconduit_api.model.JobStatus;
import io.github.mgluizbrito.mediaconduit_api.service.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.mgluizbrito.mediaconduit_api.cli.YtDlpProcessBuilder.buildYtdlpDownloadCommand;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandExecutor {

    private final ExecutorService downloadExecutor;
    private final DownloadService downloadService;

    public void submitDownloadTask(ExtractionTask task, ExtractRequestDTO request) {
        downloadExecutor.submit(() -> {

            try {
                log.info("Iniciando download para jobId: {}", task.getJobId());
                downloadService.updateTaskStatus(task.getJobId(), JobStatus.IN_PROGRESS);
                
                List<String> command = buildYtdlpDownloadCommand(task.getJobId(), request);

                ProcessBuilder pb = new ProcessBuilder(command);
                Process process = pb.start();

                Callable<String> errorReaderTask = () ->
                        new BufferedReader(new InputStreamReader(process.getErrorStream()))
                                .lines()
                                .collect(Collectors.joining(System.lineSeparator()));

                Callable<String> outputReaderTask = () ->
                        new BufferedReader(new InputStreamReader(process.getInputStream()))
                                .lines()
                                .collect(Collectors.joining(System.lineSeparator()));

                Future<String> errorPromise = downloadExecutor.submit(errorReaderTask);
                Future<String> outputPromise = downloadExecutor.submit(outputReaderTask);

                String errorMessage = errorPromise.get();
                String output = outputPromise.get();
                String finalFilePath = null;
                
                for (String line : output.split(System.lineSeparator())) {
                    log.debug("yt-dlp output: {}", line);

                    Pattern progressPattern = Pattern.compile("\\[download\\]\\s+(\\d+\\.\\d+)%");
                    Matcher matcher = progressPattern.matcher(line);

                    if (matcher.find()) {
                        int progress = (int) Double.parseDouble(matcher.group(1));
                        downloadService.updateTaskProgress(task.getJobId(), progress);
                    }

                    if (line.contains("[download] Destination:")) finalFilePath = line.substring(line.indexOf("Destination:") + 12).trim();
                }
                
                int exitCode = process.waitFor();
                
                if (exitCode == 0) {
                    downloadService.updateTaskStatus(task.getJobId(), JobStatus.COMPLETED);

                    if (finalFilePath != null) {
                        Path path = Paths.get(finalFilePath);
                        String fileName = path.getFileName().toString();
                        downloadService.updateTaskFileUrl(task.getJobId(), "downloaded-media/" + fileName);
                    }

                    log.info("Download concluído com sucesso para jobId: {}", task.getJobId());
                } else {

                    String detailedError = errorMessage.isEmpty() ? output : errorMessage;

                    downloadService.updateTaskFileUrl(task.getJobId(), null);
                    downloadService.updateTaskError(task.getJobId(),
                            "yt-dlp falhou com código de saída: " + exitCode + ": " + detailedError);

                    log.error("Download falhou para jobId: {} com código {}. Detalhes: {}", task.getJobId(), exitCode, detailedError);
                }
                
            } catch (Exception e) {
                log.error("Erro durante download para jobId: {}", task.getJobId(), e);
                downloadService.updateTaskFileUrl(task.getJobId(), null);
                downloadService.updateTaskError(task.getJobId(), e.getMessage());
            }
        });
    }
}
