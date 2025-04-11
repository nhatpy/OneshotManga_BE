package com.anime_social.models;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "manga_interaction")
public class MangaInteraction extends BaseEntity {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "time_interaction")
    @Builder.Default
    Integer time = 1;

    @OneToOne
    @MapsId
    Manga manga;
}
