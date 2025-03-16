package com.twd.Warehouse.controller;

import com.twd.Warehouse.dto.ScanHistoryDTO;
import com.twd.Warehouse.service.ScanHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/public/api/scan-history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScanHistoryController {

    private final ScanHistoryService scanHistoryService;

    @GetMapping
    public ResponseEntity<List<ScanHistoryDTO>> getAllScanHistory() {
        List<ScanHistoryDTO> history = scanHistoryService.getAllScanHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/wagon/{wagonId}")
    public ResponseEntity<List<ScanHistoryDTO>> getScanHistoryByWagonId(@PathVariable String wagonId) {
        List<ScanHistoryDTO> history = scanHistoryService.getScanHistoryByWagonId(wagonId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<List<ScanHistoryDTO>> getScanHistoryByBarcode(@PathVariable String barcode) {
        List<ScanHistoryDTO> history = scanHistoryService.getScanHistoryByBarcode(barcode);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ScanHistoryDTO>> getScanHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ScanHistoryDTO> history = scanHistoryService.getScanHistoryByDateRange(startDate, endDate);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScanHistoryDTO>> getScanHistoryByUserId(@PathVariable Integer userId) {
        List<ScanHistoryDTO> history = scanHistoryService.getScanHistoryByUserId(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportScanHistoryToCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ScanHistoryDTO> history;

        if (startDate != null && endDate != null) {
            history = scanHistoryService.getScanHistoryByDateRange(startDate, endDate);
        } else {
            history = scanHistoryService.getAllScanHistory();
        }

        // Convert history to CSV
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("ID,Barcode,Wagon ID,Status Before,Status After,Scan Time,User ID,Username,Location\n");

        for (ScanHistoryDTO entry : history) {
            csvContent.append(entry.getId()).append(",")
                    .append(entry.getBarcode()).append(",")
                    .append(entry.getWagonId()).append(",")
                    .append(entry.getStatusBefore()).append(",")
                    .append(entry.getStatusAfter()).append(",")
                    .append(entry.getScanTime()).append(",")
                    .append(entry.getUserId()).append(",")
                    .append(entry.getUsername()).append(",")
                    .append(entry.getLocation()).append("\n");
        }

        byte[] csvBytes = csvContent.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "scan_history.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}