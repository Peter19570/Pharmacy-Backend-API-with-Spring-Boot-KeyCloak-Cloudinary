package com.example.pharmacy.apps.profile.model;

import com.example.pharmacy.apps.common.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "profile_photos")
public class ProfilePhoto extends BaseEntity {

    private String url;

    private String publicId;

    @OneToOne
    @JoinColumn(name = "profile_id", unique = true)
    @JsonIgnore
    private Profile profile;
}
