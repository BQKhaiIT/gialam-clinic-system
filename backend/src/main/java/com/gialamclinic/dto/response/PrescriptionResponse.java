package com.gialamclinic.dto.response;
import com.gialamclinic.dto.request.PrescriptionItemRequest;
import lombok.*;

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
    private String doctorName;
    private String note;
    private List<PrescriptionItemResponse> medicines;
}
