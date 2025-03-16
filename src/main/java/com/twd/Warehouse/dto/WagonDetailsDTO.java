package com.twd.Warehouse.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WagonDetailsDTO {
    private Long id;
    private String wagonId;
    private String warehouseProduction;
    private String localSiteNumber;
    private String sourceArea;
    private int totalCabas;
    private int filledCabas;
    private int inProgressCabas;
    private int emptyCabas;
    private List<CabaDTO> cabas;
}
