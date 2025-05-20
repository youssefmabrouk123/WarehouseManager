package com.twd.Warehouse.controller;

import com.twd.Warehouse.dto.WagonDTO;
import com.twd.Warehouse.dto.WagonDetailsDTO;
import com.twd.Warehouse.entity.Caba;
import com.twd.Warehouse.entity.CabaStatus;
import com.twd.Warehouse.entity.Wagon;
import com.twd.Warehouse.repository.WagonRepository;
import com.twd.Warehouse.service.WagonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public/api/wagons")
@RequiredArgsConstructor
public class WagonController {

    private final WagonService wagonService;

    @Autowired
    private WagonRepository wagonRepository;

    @GetMapping
    public ResponseEntity<List<WagonDTO>> getAllWagons() {
        List<WagonDTO> wagons = wagonService.getAllWagons();
        return ResponseEntity.ok(wagons);
    }

    @GetMapping("/{wagonId}")
    public ResponseEntity<WagonDetailsDTO> getWagonDetails(@PathVariable String wagonId) {
        WagonDetailsDTO wagonDetails = wagonService.getWagonDetails(wagonId);
        return ResponseEntity.ok(wagonDetails);
    }

    @PostMapping("/{wagonId}/empty")
    public ResponseEntity<Void> emptyWagon(@PathVariable String wagonId) {
        Optional<Wagon> wagonOpt = wagonRepository.findByWagonId(wagonId);
        if (wagonOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Wagon wagon = wagonOpt.get();
        for (Caba caba : wagon.getCabas()) {
            caba.setStatus(CabaStatus.EMPTY);
        }
        wagonRepository.save(wagon);
        return ResponseEntity.ok().build();
    }



}
