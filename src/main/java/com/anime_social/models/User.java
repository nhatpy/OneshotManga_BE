package com.anime_social.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "wallet")
    @Builder.Default
    Integer wallet = 0;

    @Column(name = "role")
    HashSet<String> role;

    @Column(name = "is_verified")
    @Builder.Default
    Boolean isVerified = false;

    @Column(name = "is_warning")
    @Builder.Default
    Boolean isWarning = false;

    @Column(name = "is_banned")
    @Builder.Default
    Boolean isBanned = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    List<Notification> notifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    List<PaymentBill> paymentBills;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    List<UserReadManga> userReadMangas;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    FollowMangaList followMangaList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.ALL)
    List<Manga> mangas;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    VerifyUserCode verifyUserCode;
}
