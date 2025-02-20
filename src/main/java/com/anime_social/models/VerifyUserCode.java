package com.anime_social.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    public VerifyUserCode(User user) {
        this.user = user;
        this.code = UUID.randomUUID().toString();
    }
}
