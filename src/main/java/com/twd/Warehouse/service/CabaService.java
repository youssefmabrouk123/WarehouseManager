package com.twd.Warehouse.service;

import com.twd.Warehouse.dto.CabaDTO;
import com.twd.Warehouse.dto.CabaScanRequestDTO;
import com.twd.Warehouse.entity.*;
import com.twd.Warehouse.repository.CabaRepository;
import com.twd.Warehouse.repository.ScanHistoryRepository;
import com.twd.Warehouse.repository.UserRepository;
import com.twd.Warehouse.repository.WagonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CabaService {

    private final CabaRepository cabaRepository;
    private final WagonRepository wagonRepository;
    private final ScanHistoryRepository scanHistoryRepository;
    private final UserRepository userRepository;
    private final WagonService wagonService;

    public List<CabaDTO> getAllCabas() {
        return cabaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CabaDTO> getCabasByWagonId(String wagonId) {
        return cabaRepository.findByWagon_WagonId(wagonId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CabaDTO getCabaByBarcode(String barcode) {
        return cabaRepository.findByBarcode(barcode)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Caba not found with barcode: " + barcode));
    }

    @Transactional
    public CabaDTO createCaba(Caba caba, String wagonId) {
        // Find or create wagon
        Wagon wagon = wagonService.createWagonFromID(wagonId);

        // Set the wagon and initial status
        caba.setWagon(wagon);
        caba.setStatus(CabaStatus.EMPTY);

        Caba savedCaba = cabaRepository.save(caba);
        return convertToDTO(savedCaba);
    }

    @Transactional
    public CabaDTO scanCaba(CabaScanRequestDTO scanRequest) {
        String barcode = scanRequest.getBarcode();
        String username = scanRequest.getUsername();
        String location = scanRequest.getLocation();

        // Get caba
        Caba caba = cabaRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Caba not found with barcode: " + barcode));

        // Get user
        OurUsers user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Update caba status based on previous status
        CabaStatus statusBefore = caba.getStatus();
        CabaStatus statusAfter;

        if (statusBefore == CabaStatus.EMPTY) {
            statusAfter = CabaStatus.IN_PROGRESS;
        } else if (statusBefore == CabaStatus.IN_PROGRESS) {
            statusAfter = CabaStatus.FILLED;
        } else {
            // If already FILLED, we don't change status
            statusAfter = statusBefore;
        }

        // Update caba
        caba.setStatus(statusAfter);
        caba.setLastScanTime(LocalDateTime.now());
        caba.setLastScannedBy(username);

        // Create scan history entry
        ScanHistory scanHistory = new ScanHistory();
        scanHistory.setBarcode(barcode);
        scanHistory.setWagonId(caba.getWagon().getWagonId());
        scanHistory.setStatusBefore(statusBefore);
        scanHistory.setStatusAfter(statusAfter);
        scanHistory.setScanTime(LocalDateTime.now());
        scanHistory.setUser(user);
        scanHistory.setLocation(location);

        // Save both
        cabaRepository.save(caba);
        scanHistoryRepository.save(scanHistory);

        return convertToDTO(caba);
    }

    private CabaDTO convertToDTO(Caba caba) {
        return CabaDTO.builder()
                .id(caba.getId())
                .barcode(caba.getBarcode())
                .partNumber(caba.getPartNumber())
                .boxType(caba.getBoxType())
                .boxNumber(caba.getBoxNumber())
                .unit(caba.getUnit())
                .weight(caba.getWeight())
                .qmin(caba.getQmin())
                .qmax(caba.getQmax())
                .whLocation(caba.getWhLocation())
                .wipLocation(caba.getWipLocation())
                .mg(caba.getMg())
                .status(caba.getStatus())
                .wagonId(caba.getWagon() != null ? caba.getWagon().getWagonId() : null)
                .lastScanTime(caba.getLastScanTime())
                .lastScannedBy(caba.getLastScannedBy())
                .build();
    }
}