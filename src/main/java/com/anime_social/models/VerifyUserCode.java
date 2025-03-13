package com.anime_social.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VerifyUserCode {
    @Id
    @Column(name = "id")
    String id;
    private String code;

    @JsonIgnore
    @OneToOne
    @MapsId
    private User user;

    public VerifyUserCode(User user) {
        this.user = user;
        this.code = UUID.randomUUID().toString();
    }
}
