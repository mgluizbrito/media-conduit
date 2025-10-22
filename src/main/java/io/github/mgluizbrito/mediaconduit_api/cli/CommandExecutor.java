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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.mgluizbrito.mediaconduit_api.cli.YtDlpProcessBuilder.buildYtdlpDownloadCommand;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandExecutor {

    private final ExecutorService downloadExecutor = Executors.newFixedThreadPool(5);
    private final DownloadService downloadService;

    public void submitDownloadTask(ExtractionTask task, ExtractRequestDTO request) {
        downloadExecutor.submit(() -> {
            try {
                log.info("Iniciando download para jobId: {}", task.getJobId());
                downloadService.updateTaskStatus(task.getJobId(), JobStatus.IN_PROGRESS);
                
                List<String> command = buildYtdlpDownloadCommand(request);

                ProcessBuilder pb = new ProcessBuilder(command);
                Process process = pb.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                String finalFilePath = null;
                
                while ((line = reader.readLine()) != null) {
                    log.debug("yt-dlp output: {}", line);
                    
                    // Capturar progresso (formato: [download] XX.X% of YYY.YYMiB at ZZZ.ZZMiB/s ETA HH:MM:SS)
                    Pattern progressPattern = Pattern.compile("\\[download\\]\\s+(\\d+\\.\\d+)%");
                    Matcher matcher = progressPattern.matcher(line);

                    if (matcher.find()) {
                        int progress = (int) Double.parseDouble(matcher.group(1));
                        downloadService.updateTaskProgress(task.getJobId(), progress);
                    }

                    if (line.contains("[download] Destination:")) {
                        finalFilePath = line.substring(line.indexOf("Destination:") + 12).trim();
                    }
                }
                
                int exitCode = process.waitFor();
                
                if (exitCode == 0) {
                    downloadService.updateTaskStatus(task.getJobId(), JobStatus.COMPLETED);
                    if (finalFilePath != null) {
                        downloadService.updateTaskFileUrl(task.getJobId(), finalFilePath);
                    }
                    log.info("Download concluído com sucesso para jobId: {}", task.getJobId());
                } else {
                    downloadService.updateTaskError(task.getJobId(), "yt-dlp falhou com código de saída: " + exitCode);
                    log.error("Download falhou para jobId: {} com código: {}", task.getJobId(), exitCode);
                }
                
            } catch (Exception e) {
                log.error("Erro durante download para jobId: {}", task.getJobId(), e);
                downloadService.updateTaskError(task.getJobId(), e.getMessage());
            }
        });
    }
}
