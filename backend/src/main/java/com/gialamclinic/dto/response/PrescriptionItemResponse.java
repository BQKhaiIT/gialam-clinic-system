package com.gialamclinic.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemResponse {
    private Long medicineId;
    private String medicineName;
    private Integer quantity;
    private String dosage;
    private String instruction;
}
