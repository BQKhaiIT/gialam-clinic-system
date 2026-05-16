package com.gialamclinic.dto.request;

import com.gialamclinic.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentStatusRequest {

    @NotNull
    private AppointmentStatus status;

}