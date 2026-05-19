package com.gialamclinic.mapper;
import com.gialamclinic.dto.response.MedicalRecordResponse;
import com.gialamclinic.entity.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {
    public MedicalRecordResponse toResponse(MedicalRecord medicalRecord) {
        return MedicalRecordResponse.builder()
                .id(medicalRecord.getId())
                .appointmentId(
                        medicalRecord.getAppointment().getId()
                )
                .patientId(medicalRecord.getPatient().getId())
                .patientName(medicalRecord.getPatient().getFullName())
                .doctorId(medicalRecord.getDoctor().getId())
                .doctorName(medicalRecord.getDoctor().getFullName())
                .diagnosis(medicalRecord.getDiagnosis())
                .treatment(medicalRecord.getTreatment())
                .notes(medicalRecord.getNotes())
                .createdAt(medicalRecord.getCreatedAt())
                .build();
    }
}
