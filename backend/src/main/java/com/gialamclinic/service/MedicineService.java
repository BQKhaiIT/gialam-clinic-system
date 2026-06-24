package com.gialamclinic.service;

import com.gialamclinic.dto.request.*;
import com.gialamclinic.dto.response.MedicineResponse;
import com.gialamclinic.entity.Medicine;
import com.gialamclinic.exception.ResourceNotFoundException;
import com.gialamclinic.mapper.MedicineMapper;
import com.gialamclinic.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository repo;

    private final MedicineMapper mapper;

// CREATE

    public MedicineResponse create(
            MedicineRequest request
    ) {

        Medicine medicine =

                Medicine.builder()

                        .name(
                                request.getName()
                        )

                        .unit(
                                request.getUnit()
                        )

                        .manufacturer(
                                request.getManufacturer()
                        )

                        .stockQuantity(
                                request.getStockQuantity()
                        )

                        .price(
                                request.getPrice()
                        )

                        .description(
                                request.getDescription()
                        )

                        .build();

        return mapper.toResponse(

                repo.save(
                        medicine
                )

        );

    }

// GET ALL

    public Page<MedicineResponse> getAll(

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        return repo

                .findByIsActiveTrue(
                        pageable
                )

                .map(
                        mapper::toResponse
                );

    }

// SEARCH

    public Page<MedicineResponse> search(

            String keyword,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        return repo

                .findByNameContainingIgnoreCaseAndIsActiveTrue(

                        keyword,

                        pageable

                )

                .map(
                        mapper::toResponse
                );

    }

// GET BY ID

    public MedicineResponse getById(
            Long id
    ) {

        Medicine medicine =

                repo.findById(id)

                        .orElseThrow(

                                () -> new ResourceNotFoundException(
                                        "Medicine not found"
                                )

                        );

        return mapper.toResponse(
                medicine
        );

    }

// UPDATE

    public MedicineResponse update(

            Long id,

            MedicineRequest request

    ) {

        Medicine medicine =

                repo.findById(id)

                        .orElseThrow(

                                () -> new ResourceNotFoundException(
                                        "Medicine not found"
                                )

                        );

        medicine.setName(
                request.getName()
        );

        medicine.setUnit(
                request.getUnit()
        );

        medicine.setManufacturer(
                request.getManufacturer()
        );

        medicine.setStockQuantity(
                request.getStockQuantity()
        );

        medicine.setPrice(
                request.getPrice()
        );

        medicine.setDescription(
                request.getDescription()
        );

        return mapper.toResponse(

                repo.save(
                        medicine
                )

        );

    }

// UPDATE STOCK

    public MedicineResponse updateStock(

            Long id,

            UpdateStockRequest request

    ) {

        Medicine medicine =

                repo.findById(id)

                        .orElseThrow(

                                () -> new ResourceNotFoundException(
                                        "Medicine not found"
                                )

                        );

        medicine.setStockQuantity(

                request.getQuantity()

        );

        return mapper.toResponse(

                repo.save(
                        medicine
                )

        );

    }

// DELETE

    public void delete(
            Long id
    ) {

        Medicine medicine =

                repo.findById(id)

                        .orElseThrow(

                                () -> new ResourceNotFoundException(
                                        "Medicine not found"
                                )

                        );

        medicine.setIsActive(
                false
        );

        repo.save(
                medicine
        );

    }
}
