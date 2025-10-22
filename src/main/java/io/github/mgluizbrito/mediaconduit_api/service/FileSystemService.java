package io.github.mgluizbrito.mediaconduit_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileSystemService {

    private static final String DOWNLOAD_DIR = "./media/downloaded";
    private static final Path DOWNLOAD_PATH = Paths.get(DOWNLOAD_DIR);

    public void ensureDownloadDirectoryExists() {
        try {
            if (!Files.exists(DOWNLOAD_PATH)) {
                Files.createDirectories(DOWNLOAD_PATH);
                log.info("Diretório de download criado: {}", DOWNLOAD_PATH.toAbsolutePath());
            }
        } catch (Exception e) {
            log.error("Erro ao criar diretório de download: {}", e.getMessage());
            throw new RuntimeException("Não foi possível criar o diretório de download", e);
        }
    }

    public String getDownloadDirectory() {
        ensureDownloadDirectoryExists();
        return DOWNLOAD_PATH.toAbsolutePath().toString();
    }

    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (Exception e) {
            log.error("Erro ao obter tamanho do arquivo {}: {}", filePath, e.getMessage());
            return 0;
        }
    }
}
