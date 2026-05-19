package com.gialamclinic.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequest {
    @NotNull
    private Long appointmentId;
    @NotNull
    private String diagnosis;
    @NotNull
    private String treatment;
    @NotNull
    private String notes;
}
