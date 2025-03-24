package com.twd.Warehouse.controller;

import com.twd.Warehouse.dto.CabaDTO;
import com.twd.Warehouse.dto.CabaScanRequestDTO;
import com.twd.Warehouse.dto.CabaStatsDTO;
import com.twd.Warehouse.dto.ErrorResponse;
import com.twd.Warehouse.entity.Caba;
import com.twd.Warehouse.service.CabaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/api/cabas")
@RequiredArgsConstructor
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
    public ResponseEntity<?> scanCaba(@RequestBody CabaScanRequestDTO scanRequest) {
        try {
            CabaDTO scannedCaba = cabaService.scanCaba(scanRequest);
            return ResponseEntity.ok(scannedCaba);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("CONFLICT", e.getMessage(), scanRequest.getBarcode()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("NOT_FOUND", "Caba introuvable avec le code-barres spécifié.", scanRequest.getBarcode()));
        }
    }

    @PostMapping("/wagon/{wagonId}")
    public ResponseEntity<CabaDTO> createCaba(@RequestBody Caba caba, @PathVariable String wagonId) {
        CabaDTO createdCaba = cabaService.createCaba(caba, wagonId);
        return ResponseEntity.ok(createdCaba);
    }

    @GetMapping("/stats")
    public ResponseEntity<CabaStatsDTO> getCabaStats() {
        CabaStatsDTO stats = cabaService.getCabaStatistics();
        return ResponseEntity.ok(stats);
    }
}
