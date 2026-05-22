package com.gialamclinic.mapper;

import com.gialamclinic.dto.response.*;
import com.gialamclinic.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrescriptionMapper {

    public PrescriptionResponse toResponse(
            Prescription prescription
    ){

        List<PrescriptionItemResponse>
                medicines =

                prescription
                        .getDetails()
                        .stream()

                        .map(this::mapItem)

                        .toList();

        return PrescriptionResponse
                .builder()

                .id(
                        prescription.getId()
                )

                .medicalRecordId(

                        prescription
                                .getMedicalRecord()
                                .getId()

                )

                .doctorId(

                        prescription
                                .getDoctor()
                                .getId()

                )

                .doctorName(

                        prescription
                                .getDoctor()
                                .getFullName()

                )

                .note(
                        prescription.getNote()
                )

                .medicines(
                        medicines
                )

                .build();

    }

    private PrescriptionItemResponse
    mapItem(

            PrescriptionDetail detail

    ){

        return PrescriptionItemResponse
                .builder()

                .medicineId(

                        detail
                                .getMedicine()
                                .getId()

                )

                .medicineName(

                        detail
                                .getMedicine()
                                .getName()

                )

                .quantity(
                        detail.getQuantity()
                )

                .dosage(
                        detail.getDosage()
                )

                .instruction(
                        detail.getInstruction()
                )

                .build();

    }

}