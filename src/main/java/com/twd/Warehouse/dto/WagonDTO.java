package com.twd.Warehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WagonDTO {
    private Long id;
    private String wagonId;
    private String warehouseProduction;
    private String localSiteNumber;
    private String sourceArea;
    private int totalCabas;
    private int filledCabas;
    private double fillRate;
}