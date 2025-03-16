package com.twd.Warehouse.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cabas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String barcode;

    private String partNumber; // PN
    private String boxType;
    private String boxNumber;
    private String unit;
    private Double weight;
    private Integer qmin;
    private Integer qmax = 0;
    private String whLocation;
    private String wipLocation;
    private String mg;

    @Enumerated(EnumType.STRING)
    private CabaStatus status = CabaStatus.EMPTY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wagon_id")
    private Wagon wagon;

    private LocalDateTime lastScanTime;

    private String lastScannedBy;
}
