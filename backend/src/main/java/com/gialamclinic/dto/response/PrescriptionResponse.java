package com.gialamclinic.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {

    private Long id;

    private Long medicalRecordId;

    private Long doctorId;

    private String patientName;

    private String doctorName;

    private String note;

    private Integer totalMedicines;

    private LocalDateTime createdAt;

    private List<PrescriptionItemResponse> medicines;

}