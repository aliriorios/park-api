package br.com.example.park_api.entity;

import br.com.example.park_api.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class) // Enabling the Entity for the Audit Process
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_CLIENT;

    @Column(name = "created_date")
    @CreatedDate // JPA Auditing
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate // JPA Auditing
    private LocalDateTime modifiedDate;

    @Column(name = "created_by")
    @CreatedBy // JPA Auditing
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy // JPA Auditing
    private String modifiedBy;
}
