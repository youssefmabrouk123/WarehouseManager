package com.twd.Warehouse.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.twd.Warehouse.dto.CabaDTO;
import com.twd.Warehouse.dto.CabaScanRequestDTO;
import com.twd.Warehouse.dto.CabaStatsDTO;
import com.twd.Warehouse.entity.*;
import com.twd.Warehouse.repository.CabaRepository;
import com.twd.Warehouse.repository.ScanHistoryRepository;
import com.twd.Warehouse.repository.UserRepository;
import com.twd.Warehouse.repository.WagonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CabaService {
    private static final Logger logger = LoggerFactory.getLogger(CabaService.class);
    private final CabaRepository cabaRepository;
    private final WagonRepository wagonRepository;
    private final ScanHistoryRepository scanHistoryRepository;
    private final UserRepository userRepository;
    private final WagonService wagonService;
    private final SocketIOServer socketIOServer;

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

        // Get user
        OurUsers user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Check if user already has an IN_PROGRESS Caba
        Optional<Caba> userInProgressCaba = cabaRepository.findByLastScannedByAndStatus(username, CabaStatus.IN_PROGRESS);
        if (userInProgressCaba.isPresent() && !userInProgressCaba.get().getBarcode().equals(barcode)) {
            throw new IllegalStateException("User " + username + " already has an IN_PROGRESS Caba (barcode: " +
                    userInProgressCaba.get().getBarcode() + "). Complete it before scanning a new one.");
        }

        // Get caba
        Caba caba = cabaRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Caba not found with barcode: " + barcode));

        // Update caba status based on previous status
        CabaStatus statusBefore = caba.getStatus();
        CabaStatus statusAfter;

        if (statusBefore == CabaStatus.EMPTY) {
            statusAfter = CabaStatus.IN_PROGRESS;
        } else if (statusBefore == CabaStatus.IN_PROGRESS && caba.getLastScannedBy().equals(username)) {
            statusAfter = CabaStatus.FILLED;
        } else if (statusBefore == CabaStatus.IN_PROGRESS) {
            throw new IllegalStateException("Caba " + barcode + " is already IN_PROGRESS by another user: " +
                    caba.getLastScannedBy());
        } else {
            statusAfter = statusBefore;
        }

        // Update caba
        caba.setStatus(statusAfter);
        caba.setLastScanTime(LocalDateTime.now());
        caba.setLastScannedBy(username);

        // Create scan history entry
        ScanHistory scanHistory = new ScanHistory();
        scanHistory.setBarcode(barcode);
        scanHistory.setWagonId(caba.getWagon() != null ? caba.getWagon().getWagonId() : null);
        scanHistory.setStatusBefore(statusBefore);
        scanHistory.setStatusAfter(statusAfter);
        scanHistory.setScanTime(LocalDateTime.now());
        scanHistory.setUser(user);
        scanHistory.setLocation(location);

        // Save both
        cabaRepository.save(caba);
        scanHistoryRepository.save(scanHistory);

        CabaDTO result = convertToDTO(caba);

        // Emit Socket.IO event
        socketIOServer.getBroadcastOperations().sendEvent("cabaUpdate", result);

        return result;
    }
    private CabaDTO convertToDTO(Caba caba) {

        LocalDateTime lastScanTime = caba.getLastScanTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = (lastScanTime != null) ? lastScanTime.format(formatter) : null; // Remplacez "N/A" par ce que vous voulez
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
                .lastScanTime(formattedDate)
                .lastScannedBy(caba.getLastScannedBy())
                .build();
    }


    public CabaStatsDTO getCabaStatistics() {
        // Simuler des données (remplacez par une vraie logique DB si nécessaire)
        CabaStatsDTO stats = new CabaStatsDTO();
        stats.setTotalCabas(100);       // Exemple
        stats.setEmptyCabas(30);
        stats.setInProgressCabas(40);
        stats.setFilledCabas(30);

        logger.info("Returning Caba statistics: {}", stats);
        return stats;
    }
}
