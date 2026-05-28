package com.gialamclinic.service;

import com.gialamclinic.dto.request.*;
import com.gialamclinic.dto.response.PrescriptionResponse;
import com.gialamclinic.entity.*;
import com.gialamclinic.enums.AppointmentStatus;
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

    private final AppointmentRepository appointmentRepo;
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

        Appointment appointment =
                medicalRecord.getAppointment();

        if(
                appointment.getStatus()
                        != AppointmentStatus.IN_PROGRESS
        ){

            throw new BadRequestException(
                    "Prescription can only be created for in-progress appointments"
            );

        }

        if(

                prescriptionRepo
                        .existsByMedicalRecordId(
                                medicalRecord.getId()
                        )

        ){

            throw new BadRequestException(
                    "Prescription already exists"
            );

        }

        if(

                request.getMedicines() == null

                        ||

                        request.getMedicines().isEmpty()

        ){

            throw new BadRequestException(
                    "Prescription medicines cannot be empty"
            );

        }

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
        Set<Long> medicineIds =
                new HashSet<>();
        for(

                PrescriptionItemRequest item

                :

                request.getMedicines()

        ){
            if(

                    !medicineIds.add(
                            item.getMedicineId()
                    )

            ){

                throw new BadRequestException(
                        "Duplicate medicine in prescription"
                );

            }

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

// AUTO COMPLETE APPOINTMENT

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        appointmentRepo.save(
                appointment
        );

        return mapper.toResponse(
                prescription
        );

    }
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAll(){

        return prescriptionRepo
                .findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }
}