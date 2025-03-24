package com.twd.Warehouse.dto;


import lombok.Data;

@Data
public class CabaStatsDTO {
    private long totalCabas;
    private long emptyCabas;
    private long inProgressCabas;
    private long filledCabas;
}