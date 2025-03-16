package com.twd.Warehouse.service;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.twd.Warehouse.entity.Caba;
import com.twd.Warehouse.entity.CabaStatus;
import com.twd.Warehouse.entity.Wagon;
import com.twd.Warehouse.repository.CabaRepository;
import com.twd.Warehouse.repository.WagonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataImportService {

    private final CabaRepository cabaRepository;
    private final WagonRepository wagonRepository;
    private final WagonService wagonService;

    @Transactional
    public void importCabasFromCSV(MultipartFile file) throws IOException, CsvValidationException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            // Read header
            String[] header = csvReader.readNext();

            // Find column indexes
            Map<String, Integer> columnIndexes = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                columnIndexes.put(header[i].replace("\"", ""), i);
            }

            // Check required columns
            checkRequiredColumns(columnIndexes);

            // Process rows
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                processCabaRow(row, columnIndexes);
            }
        }
    }

    private void checkRequiredColumns(Map<String, Integer> columnIndexes) {
        String[] requiredColumns = {"Barcode", "PN", "Box Type", "Box Number", "Unit", "Weight",
                "QMIN", "QMAX", "WH Location", "WIP Location", "ID/WAGON", "MG"};

        for (String column : requiredColumns) {
            if (!columnIndexes.containsKey(column)) {
                throw new IllegalArgumentException("Required column missing: " + column);
            }
        }
    }

    private void processCabaRow(String[] row, Map<String, Integer> columnIndexes) {
        // Extract data
        String barcode = row[columnIndexes.get("Barcode")].replace("\"", "");
        String partNumber = row[columnIndexes.get("PN")].replace("\"", "");
        String boxType = row[columnIndexes.get("Box Type")].replace("\"", "");
        String boxNumber = row[columnIndexes.get("Box Number")].replace("\"", "");
        String unit = row[columnIndexes.get("Unit")].replace("\"", "");
        String weightStr = row[columnIndexes.get("Weight")].replace("\"", "");
        String qminStr = row[columnIndexes.get("QMIN")].replace("\"", "");
        String qmaxStr = row[columnIndexes.get("QMAX")].replace("\"", "");
        String whLocation = row[columnIndexes.get("WH Location")].replace("\"", "");
        String wipLocation = row[columnIndexes.get("WIP Location")].replace("\"", "");
        String wagonId = row[columnIndexes.get("ID/WAGON")].replace("\"", "");
        String mg = row[columnIndexes.get("MG")].replace("\"", "");

        // Parse numeric values
        Double weight = weightStr.isEmpty() ? null : Double.parseDouble(weightStr);
        Integer qmin = qminStr.isEmpty() ? null : Integer.parseInt(qminStr);
        Integer qmax = qmaxStr.isEmpty() ? null : Integer.parseInt(qmaxStr);

        // Check if caba already exists
        Optional<Caba> existingCaba = cabaRepository.findByBarcode(barcode);
        Caba caba;

        if (existingCaba.isPresent()) {
            // Update existing caba
            caba = existingCaba.get();
            caba.setPartNumber(partNumber);
            caba.setBoxType(boxType);
            caba.setBoxNumber(boxNumber);
            caba.setUnit(unit);
            caba.setWeight(weight);
            caba.setQmin(qmin);
            caba.setQmax(qmax);
            caba.setWhLocation(whLocation);
            caba.setWipLocation(wipLocation);
            caba.setMg(mg);
        } else {
            // Create new caba
            caba = new Caba();
            caba.setBarcode(barcode);
            caba.setPartNumber(partNumber);
            caba.setBoxType(boxType);
            caba.setBoxNumber(boxNumber);
            caba.setUnit(unit);
            caba.setWeight(weight);
            caba.setQmin(qmin);
            caba.setQmax(qmax);
            caba.setWhLocation(whLocation);
            caba.setWipLocation(wipLocation);
            caba.setMg(mg);
            caba.setStatus(CabaStatus.EMPTY);
        }

        // Get or create wagon
        Wagon wagon = wagonService.createWagonFromID(wagonId);

        // Set wagon to caba
        caba.setWagon(wagon);

        // Save caba
        cabaRepository.save(caba);
    }
}