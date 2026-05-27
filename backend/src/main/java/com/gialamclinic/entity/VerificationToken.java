package com.gialamclinic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VerificationToken {

    @Id

    @GeneratedValue(
            strategy=
                    GenerationType.IDENTITY
    )

    private Long id;

    @Column(
            unique=true
    )

    private String token;

    @OneToOne

    @JoinColumn(
            name="user_id"
    )

    private User user;

    private LocalDateTime expiredAt;

}