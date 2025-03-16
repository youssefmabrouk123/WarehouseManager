package com.twd.Warehouse.dto;

import lombok.Data;

@Data
public class CabaScanRequestDTO {
    private String barcode;
    private String username;
    private String location;
}
