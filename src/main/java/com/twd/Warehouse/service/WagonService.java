package com.twd.Warehouse.service;
import com.twd.Warehouse.dto.CabaDTO;
import com.twd.Warehouse.dto.WagonDTO;
import com.twd.Warehouse.dto.WagonDetailsDTO;
import com.twd.Warehouse.entity.Caba;
import com.twd.Warehouse.entity.CabaStatus;
import com.twd.Warehouse.entity.Wagon;
import com.twd.Warehouse.repository.CabaRepository;
import com.twd.Warehouse.repository.WagonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WagonService {

    private final WagonRepository wagonRepository;
    private final CabaRepository cabaRepository;

    public List<WagonDTO> getAllWagons() {
        return wagonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WagonDetailsDTO getWagonDetails(String wagonId) {
        Optional<Wagon> wagonOptional = wagonRepository.findByWagonId(wagonId);

        if (wagonOptional.isEmpty()) {
            throw new RuntimeException("Wagon not found with ID: " + wagonId);
        }

        Wagon wagon = wagonOptional.get();
        List<Caba> cabas = cabaRepository.findByWagon_WagonId(wagonId);

        return WagonDetailsDTO.builder()
                .id(wagon.getId())
                .wagonId(wagon.getWagonId())
                .warehouseProduction(wagon.getWarehouseProduction())
                .localSiteNumber(wagon.getLocalSiteNumber())
                .sourceArea(wagon.getSourceArea())
                .totalCabas(cabas.size())
                .filledCabas((int) cabas.stream().filter(c -> c.getStatus() == CabaStatus.FILLED).count())
                .inProgressCabas((int) cabas.stream().filter(c -> c.getStatus() == CabaStatus.IN_PROGRESS).count())
                .emptyCabas((int) cabas.stream().filter(c -> c.getStatus() == CabaStatus.EMPTY).count())
                .cabas(cabas.stream().map(this::convertCabaToDTO).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public Wagon createWagonFromID(String wagonId) {
        // Check if wagon already exists
        Optional<Wagon> existingWagon = wagonRepository.findByWagonId(wagonId);
        if (existingWagon.isPresent()) {
            return existingWagon.get();
        }

        // Parse wagonId format: WIP1-SEG94-W2
        String[] parts = wagonId.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid wagon ID format");
        }

        Wagon wagon = new Wagon();
        wagon.setWagonId(wagonId);
        wagon.setWarehouseProduction(parts[0]);
        wagon.setLocalSiteNumber(parts[1]);
        wagon.setSourceArea(parts[2]);

        return wagonRepository.save(wagon);
    }

    private WagonDTO convertToDTO(Wagon wagon) {
        long totalCabas = cabaRepository.count();
        long filledCabas = cabaRepository.countByWagonIdAndStatus(wagon.getWagonId(), CabaStatus.FILLED);

        return WagonDTO.builder()
                .id(wagon.getId())
                .wagonId(wagon.getWagonId())
                .warehouseProduction(wagon.getWarehouseProduction())
                .localSiteNumber(wagon.getLocalSiteNumber())
                .sourceArea(wagon.getSourceArea())
                .totalCabas((int) totalCabas)
                .filledCabas((int) filledCabas)
                .fillRate(totalCabas > 0 ? (filledCabas * 100.0 / totalCabas) : 0)
                .build();
    }

    private CabaDTO convertCabaToDTO(Caba caba) {
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
                .lastScanTime(caba.getLastScanTime())
                .lastScannedBy(caba.getLastScannedBy())
                .build();
    }
}