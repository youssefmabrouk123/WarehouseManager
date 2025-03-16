package com.twd.Warehouse.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scan_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String barcode;
    private String wagonId;

    @Enumerated(EnumType.STRING)
    private CabaStatus statusBefore;

    @Enumerated(EnumType.STRING)
    private CabaStatus statusAfter;

    private LocalDateTime scanTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private OurUsers user;

    private String location;
}