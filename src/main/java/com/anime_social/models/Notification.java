package com.anime_social.models;

import com.anime_social.enums.NotiType;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    NotiType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
