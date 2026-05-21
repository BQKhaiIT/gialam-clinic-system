package com.gialamclinic.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponse {
    private Long id;
    private String name;
    private String unit;
    private String manufacturer;
    private Integer stockQuantity;
    private Double price;
    private String description;
}
