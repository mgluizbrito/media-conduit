package io.github.mgluizbrito.mediaconduit_api.cli;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractRequestDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractConfigs.ExtractConfigDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractConfigs.YoutubeExtractConfigDTO;
import io.github.mgluizbrito.mediaconduit_api.exceptions.MediaExtractionException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class YtDlpProcessBuilder {

    public static String exeYtDlpCommand(List<String> command) {
        Process process = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            process = pb.start();
            Process finalProcess = process;

            // Leitura do STDERR (saída de erro)
            Callable<String> errorReader = () ->
                    new BufferedReader(new InputStreamReader(finalProcess.getErrorStream()))
                            .lines()
                            .collect(Collectors.joining(System.lineSeparator()));

            // Leitura do STDOUT (saida normal)
            Callable<String> outputReader = () ->
                    new BufferedReader(new InputStreamReader(finalProcess.getInputStream()))
                            .lines()
                            .collect(Collectors.joining(System.lineSeparator()));

            ExecutorService executor = Executors.newFixedThreadPool(2);

            Future<String> errorPromise = executor.submit(errorReader);
            Future<String> outputPromise = executor.submit(outputReader);

            String output = outputPromise.get();
            String error = errorPromise.get();

            if (process.waitFor() != 0)
                throw new MediaExtractionException("Failed to extract metadata from yt-dlp. [Exit code: " + process.waitFor() + "]", error);

            return output;

        } catch (MediaExtractionException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Error running the yt-dlp process: ", e);

        } finally {
            if (process != null) process.destroy();
        }
    }

    public static List<String> buildYtdlpDownloadCommand(UUID jobId, ExtractRequestDTO request) {
        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        
        // URL do vídeo
        command.add(request.mediaIdentifier());

        String outputTemplate = String.format("./media/downloaded/%s.%s", jobId, request.format());
        command.add("-o");
        command.add(outputTemplate);
        
        // Configurações de formato baseadas na qualidade
        String formatString = request.format();
        String formatSelector = buildFormatSelector(formatString, request.config());
        command.add("--format");
        command.add(formatSelector);
        
        // Recodificar se necessário
        command.add("--recode-video");
        command.add(formatString);
        
        // Configurações adicionais
        command.add("--newline");
        command.add("--progress");
        command.add("--add-header");
        command.add("User-Agent: MediaConduit-API-Client");
        
        // Configurações específicas do extrator se disponível
        if (request.config() != null) {
            addExtractorSpecificConfig(command, request.config());
        }
        
        return command;
    }
    
    private static String buildFormatSelector(String format, ExtractConfigDTO config) {
        if (config == null) return String.format("bestvideo[ext=%s]+bestaudio/best", format);
        
        // Mapear qualidade (1-5) para resoluções do yt-dlp
        int quality = config.getQuality();
        String resolution = mapQualityToResolution(quality);
        
        if (resolution != null) {
            return String.format("bestvideo[height<=%s][ext=%s]+bestaudio/best[height<=%s]", 
                                resolution, format, resolution);
        }
        
        return String.format("bestvideo[ext=%s]+bestaudio/best", format);
    }
    
    private static String mapQualityToResolution(int quality) {
        return switch (quality) {
            case 1 -> "360";
            case 2 -> "480";
            case 3 -> "720";
            case 4 -> "1080";
            case 5 -> "2160"; // 4K
            default -> null;
        };
    }
    
    private static void addExtractorSpecificConfig(List<String> command, ExtractConfigDTO config) {
        // Adicionar configurações específicas baseadas no tipo de extrator
        // Por exemplo, para YouTube, podemos adicionar configurações específicas
        if (config instanceof YoutubeExtractConfigDTO) {
            // Adicionar configurações específicas do YouTube se necessário
            // command.add("--youtube-skip-dash-manifest");
        }
    }
}
