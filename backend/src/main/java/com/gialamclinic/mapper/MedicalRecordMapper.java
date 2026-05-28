package com.gialamclinic.mapper;

import com.gialamclinic.dto.response.MedicalRecordResponse;
import com.gialamclinic.entity.MedicalRecord;

import com.gialamclinic.repository.PrescriptionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalRecordMapper {

    private final PrescriptionRepository prescriptionRepo;

    public MedicalRecordResponse toResponse(
            MedicalRecord medicalRecord
    ) {

        boolean hasPrescription =

                prescriptionRepo
                        .existsByMedicalRecordId(
                                medicalRecord.getId()
                        );

        return MedicalRecordResponse.builder()

                .id(
                        medicalRecord.getId()
                )

                .appointmentId(
                        medicalRecord.getAppointment().getId()
                )

                .patientId(
                        medicalRecord.getPatient().getId()
                )

                .patientName(
                        medicalRecord.getPatient().getFullName()
                )

                .doctorId(
                        medicalRecord.getDoctor().getId()
                )

                .doctorName(
                        medicalRecord.getDoctor().getFullName()
                )

                .diagnosis(
                        medicalRecord.getDiagnosis()
                )

                .treatment(
                        medicalRecord.getTreatment()
                )

                .notes(
                        medicalRecord.getNotes()
                )

                .hasPrescription(
                        hasPrescription
                )

                .createdAt(
                        medicalRecord.getCreatedAt()
                )

                .build();

    }

}