package com.twd.Warehouse.controller;

import com.twd.Warehouse.dto.CabaDTO;
import com.twd.Warehouse.dto.CabaScanRequestDTO;
import com.twd.Warehouse.entity.Caba;
import com.twd.Warehouse.service.CabaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/api/cabas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CabaController {

    private final CabaService cabaService;

    @GetMapping
    public ResponseEntity<List<CabaDTO>> getAllCabas() {
        List<CabaDTO> cabas = cabaService.getAllCabas();
        return ResponseEntity.ok(cabas);
    }

    @GetMapping("/wagon/{wagonId}")
    public ResponseEntity<List<CabaDTO>> getCabasByWagonId(@PathVariable String wagonId) {
        List<CabaDTO> cabas = cabaService.getCabasByWagonId(wagonId);
        return ResponseEntity.ok(cabas);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<CabaDTO> getCabaByBarcode(@PathVariable String barcode) {
        CabaDTO caba = cabaService.getCabaByBarcode(barcode);
        return ResponseEntity.ok(caba);
    }

    @PostMapping("/scan")
    public ResponseEntity<CabaDTO> scanCaba(@RequestBody CabaScanRequestDTO scanRequest) {
        CabaDTO scannedCaba = cabaService.scanCaba(scanRequest);
        return ResponseEntity.ok(scannedCaba);
    }

    @PostMapping("/wagon/{wagonId}")
    public ResponseEntity<CabaDTO> createCaba(@RequestBody Caba caba, @PathVariable String wagonId) {
        CabaDTO createdCaba = cabaService.createCaba(caba, wagonId);
        return ResponseEntity.ok(createdCaba);
    }
}
