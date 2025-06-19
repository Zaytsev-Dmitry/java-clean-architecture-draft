package com.clean.architecture.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", columnDefinition = "varchar")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar")
    private String lastName;

    @Column(name = "email", columnDefinition = "varchar")
    private String email;

    @Column(name = "username", columnDefinition = "varchar")
    private String username;

    //timestamp UTC
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp UTC")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", columnDefinition = "boolean")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar")
    private UserRole role;
}
