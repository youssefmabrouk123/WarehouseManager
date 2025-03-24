package com.twd.Warehouse.dto;


import com.twd.Warehouse.entity.CabaStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CabaDTO {
    private Long id;
    private String barcode;
    private String partNumber;
    private String boxType;
    private String boxNumber;
    private String unit;
    private Double weight;
    private Integer qmin;
    private Integer qmax;
    private String whLocation;
    private String wipLocation;
    private String mg;
    private CabaStatus status;
    private String wagonId;
    private String lastScanTime;
    private String lastScannedBy;
//    private String lastTime;
}
