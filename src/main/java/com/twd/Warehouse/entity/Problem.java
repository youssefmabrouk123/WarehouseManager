package com.twd.Warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String userId;
    private String problemType;
    private String description;
    private String status = "pending"; // e.g., pending, resolved
}