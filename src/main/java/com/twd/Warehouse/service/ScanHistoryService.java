package com.twd.Warehouse.service;

import com.twd.Warehouse.dto.ScanHistoryDTO;
import com.twd.Warehouse.entity.ScanHistory;
import com.twd.Warehouse.repository.ScanHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScanHistoryService {

    private final ScanHistoryRepository scanHistoryRepository;

    public List<ScanHistoryDTO> getAllScanHistory() {
        return scanHistoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScanHistoryDTO> getScanHistoryByWagonId(String wagonId) {
        return scanHistoryRepository.findByWagonId(wagonId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScanHistoryDTO> getScanHistoryByBarcode(String barcode) {
        return scanHistoryRepository.findByBarcode(barcode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScanHistoryDTO> getScanHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return scanHistoryRepository.findByScanTimeBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScanHistoryDTO> getScanHistoryByUserId(Integer userId) {
        return scanHistoryRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ScanHistoryDTO convertToDTO(ScanHistory scanHistory) {
        return ScanHistoryDTO.builder()
                .id(scanHistory.getId())
                .barcode(scanHistory.getBarcode())
                .wagonId(scanHistory.getWagonId())
                .statusBefore(scanHistory.getStatusBefore())
                .statusAfter(scanHistory.getStatusAfter())
                .scanTime(scanHistory.getScanTime())
                .userId(scanHistory.getUser() != null ? scanHistory.getUser().getId() : null)
                .username(scanHistory.getUser() != null ? scanHistory.getUser().getUsername() : null)
                .location(scanHistory.getLocation())
                .build();
    }
}
