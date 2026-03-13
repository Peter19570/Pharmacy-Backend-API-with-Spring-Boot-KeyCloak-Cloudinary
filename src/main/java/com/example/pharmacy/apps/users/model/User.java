package com.example.pharmacy.apps.users.model;

import com.example.pharmacy.apps.common.model.BaseEntity;
import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.sales.model.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(updatable = false, unique = true, nullable = false)
    private String keyCloakId;

    private String email;

    private String firstName;

    private String lastName;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Sale> sales;


}
