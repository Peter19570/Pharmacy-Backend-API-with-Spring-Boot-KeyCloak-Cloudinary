package com.example.pharmacy.apps.users.model;

import com.example.pharmacy.apps.common.model.BaseEntity;
import com.example.pharmacy.apps.profile.model.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

}
