package com.gialamclinic.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemRequest {
    @NotNull
    private Long medicineId;
    @NotNull
    private Integer quantity;
    private String dosage;
    private String instruction;
}
