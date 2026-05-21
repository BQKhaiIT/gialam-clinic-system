package com.gialamclinic.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String unit;

    private String manufacturer;

    private Integer stockQuantity;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;
}
