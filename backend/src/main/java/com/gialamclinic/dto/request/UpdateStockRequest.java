package com.gialamclinic.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStockRequest {
    @NotNull
    private Integer quantity;
}
