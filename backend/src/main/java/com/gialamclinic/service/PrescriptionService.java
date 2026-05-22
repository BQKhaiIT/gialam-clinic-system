package com.gialamclinic.service;

import com.gialamclinic.dto.request.*;
import com.gialamclinic.dto.response.PrescriptionResponse;
import com.gialamclinic.entity.*;
import com.gialamclinic.exception.*;
import com.gialamclinic.mapper.PrescriptionMapper;
import com.gialamclinic.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context
        .SecurityContextHolder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation
        .Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository
            prescriptionRepo;

    private final MedicineRepository
            medicineRepo;

    private final MedicalRecordRepository
            medicalRecordRepo;

    private final UserRepository
            userRepo;

    private final PrescriptionMapper
            mapper;


    @Transactional
    public PrescriptionResponse create(

            PrescriptionRequest request

    ){

        MedicalRecord medicalRecord =

                medicalRecordRepo
                        .findById(

                                request
                                        .getMedicalRecordId()

                        )

                        .orElseThrow(

                                ()->new ResourceNotFoundException(
                                        "Medical record not found"
                                )

                        );


        String username =

                SecurityContextHolder

                        .getContext()

                        .getAuthentication()

                        .getName();


        User doctor =

                userRepo

                        .findByUsername(
                                username
                        )

                        .orElseThrow(

                                ()->new ResourceNotFoundException(
                                        "Doctor not found"
                                )

                        );


        Prescription prescription =

                Prescription.builder()

                        .medicalRecord(
                                medicalRecord
                        )

                        .doctor(
                                doctor
                        )

                        .note(
                                request.getNote()
                        )

                        .build();


        prescription =

                prescriptionRepo
                        .save(
                                prescription
                        );


        List<PrescriptionDetail>
                details =

                new ArrayList<>();


        for(

                PrescriptionItemRequest item

                :

                request.getMedicines()

        ){

            Medicine medicine =

                    medicineRepo
                            .findById(

                                    item.getMedicineId()

                            )

                            .orElseThrow(

                                    ()->
                                            new ResourceNotFoundException(
                                                    "Medicine not found"
                                            )

                            );


            // INVENTORY VALIDATION

            if(

                    medicine.getStockQuantity()

                            <

                            item.getQuantity()

            ){

                throw new BadRequestException(

                        medicine.getName()

                                +

                                " out of stock"

                );

            }


            // DEDUCT INVENTORY

            medicine.setStockQuantity(

                    medicine.getStockQuantity()

                            -

                            item.getQuantity()

            );

            medicineRepo.save(
                    medicine
            );


            PrescriptionDetail detail =

                    PrescriptionDetail
                            .builder()

                            .prescription(
                                    prescription
                            )

                            .medicine(
                                    medicine
                            )

                            .quantity(
                                    item.getQuantity()
                            )

                            .dosage(
                                    item.getDosage()
                            )

                            .instruction(
                                    item.getInstruction()
                            )

                            .build();


            details.add(
                    detail
            );

        }


        prescription.setDetails(
                details
        );

        prescriptionRepo.save(
                prescription
        );

        return mapper.toResponse(
                prescription
        );

    }

}