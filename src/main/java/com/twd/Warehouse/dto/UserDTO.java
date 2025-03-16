package com.twd.Warehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String nom;
    private String email;
    private String username;
    private String fullName;
    private String role;
    private boolean active;
}