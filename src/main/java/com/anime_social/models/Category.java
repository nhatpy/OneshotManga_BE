package com.anime_social.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "category")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Column(name = "name", unique = true)
    String name;

    @Column(name = "slug", unique = true)
    String slug;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    String description;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<CategoryManga> categoryMangas;
}
