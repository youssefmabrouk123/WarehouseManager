package com.twd.Warehouse.controller;

import com.twd.Warehouse.dto.WagonDTO;
import com.twd.Warehouse.dto.WagonDetailsDTO;
import com.twd.Warehouse.service.WagonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/api/wagons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WagonController {

    private final WagonService wagonService;

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
}
