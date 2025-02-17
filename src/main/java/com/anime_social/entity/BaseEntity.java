package com.anime_social.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {
    @CreatedDate
    @Column(name = "create_at", updatable = false, nullable = false)
    Date createAt;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    Date updateAt;
}
