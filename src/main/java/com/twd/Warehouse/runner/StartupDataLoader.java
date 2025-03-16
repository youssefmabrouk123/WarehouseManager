package com.twd.Warehouse.runner;
import com.opencsv.exceptions.CsvValidationException;
import com.twd.Warehouse.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class StartupDataLoader {

    private final DataImportService dataImportService;

    @Bean
    public ApplicationRunner loadDataOnStartup() {
        return args -> {
            try {
                // Charger le fichier depuis `resources/static/data.csv`
                ClassPathResource resource = new ClassPathResource("caba-database.csv");
                Path tempFile = Files.createTempFile("data", ".csv");
                Files.copy(resource.getInputStream(), tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Convertir en MultipartFile
                MultipartFile multipartFile = new CustomMultipartFile(tempFile);

                // Importer les données
                dataImportService.importCabasFromCSV(multipartFile);

                System.out.println("✅ Importation du fichier CSV réussie.");
            } catch (IOException | CsvValidationException e) {
                System.err.println("❌ Erreur lors de l'importation du fichier CSV : " + e.getMessage());
            }
        };
    }
}
