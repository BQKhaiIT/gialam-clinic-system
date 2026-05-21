package com.gialamclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MedicineRequest {
    @NotBlank
    private String name;
    private String unit;
    private String manufacturer;
    private Integer stockQuantity;
    private Double price;
    private String description;
}
