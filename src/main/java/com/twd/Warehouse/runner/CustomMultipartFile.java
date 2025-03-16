package com.twd.Warehouse.runner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomMultipartFile implements MultipartFile {

    private final Path filePath;

    public CustomMultipartFile(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getName() {
        return filePath.getFileName().toString();
    }

    @Override
    public String getOriginalFilename() {
        return filePath.getFileName().toString();
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }

    @Override
    public boolean isEmpty() {
        return filePath.toFile().length() == 0;
    }

    @Override
    public long getSize() {
        return filePath.toFile().length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(filePath);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(filePath.toFile());
    }

    @Override
    public void transferTo(File dest) throws IOException {
        Files.copy(filePath, dest.toPath());
    }
}
