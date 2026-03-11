package com.example.pharmacy.apps.profile.model;

import com.example.pharmacy.apps.common.model.BaseEntity;
import com.example.pharmacy.apps.users.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "profiles")
public class Profile extends BaseEntity {

    private Integer age;

    private String city;

    private String country;

    @Column(length = 15)
    private String contact;

    @Column(length = 350)
    private String bio;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfilePhoto profilePhoto;

    @OneToOne()
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;
}
