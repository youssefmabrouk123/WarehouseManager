package com.twd.Warehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wagons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wagon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String wagonId; // Format: WIP1-SEG94-W2

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Caba> cabas = new ArrayList<>();

    private String warehouseProduction;
    private String localSiteNumber;
    private String sourceArea;
}