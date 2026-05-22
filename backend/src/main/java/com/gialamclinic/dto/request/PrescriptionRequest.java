package com.gialamclinic.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequest {
    @NotNull
    private Long medicalRecordId;
    private String note;
    private List<PrescriptionItemRequest> medicines;
}
