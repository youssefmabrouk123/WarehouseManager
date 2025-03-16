package com.twd.Warehouse.dto;

import com.twd.Warehouse.entity.CabaStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScanHistoryDTO {
    private Long id;
    private String barcode;
    private String wagonId;
    private CabaStatus statusBefore;
    private CabaStatus statusAfter;
    private LocalDateTime scanTime;
    private Integer userId;
    private String username;
    private String location;
}