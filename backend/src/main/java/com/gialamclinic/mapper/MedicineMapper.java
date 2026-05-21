package com.gialamclinic.mapper;
import com.gialamclinic.dto.response.MedicineResponse;
import com.gialamclinic.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {
    public MedicineResponse toResponse(Medicine medicine) {
        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .unit(medicine.getUnit())
                .manufacturer(medicine.getManufacturer())
                .stockQuantity(medicine.getStockQuantity())
                .price(medicine.getPrice())
                .description(medicine.getDescription())
                .build();
    }
}
