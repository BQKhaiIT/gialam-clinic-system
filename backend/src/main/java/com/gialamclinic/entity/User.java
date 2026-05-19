package com.gialamclinic.entity;

import com.gialamclinic.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter

public class User extends BaseEntity {

    @Column(unique = true)
    private String username;
    private String password;

    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
