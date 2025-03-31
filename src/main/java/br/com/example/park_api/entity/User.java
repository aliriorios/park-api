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
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class) // Enabling the Entity for the Audit Process
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ToString.Include
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    @ToString.Include
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_CLIENT;

    // Auditor Aware fields -------------------------------
    @Column(name = "created_date")
    @Setter(AccessLevel.NONE)
    @CreatedDate // JPA Auditing
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @Setter(AccessLevel.NONE)
    @LastModifiedDate // JPA Auditing
    private LocalDateTime modifiedDate;

    @Column(name = "created_by")
    @Setter(AccessLevel.NONE)
    @CreatedBy // JPA Auditing
    private String createdBy;

    @Column(name = "modified_by")
    @Setter(AccessLevel.NONE)
    @LastModifiedBy // JPA Auditing
    private String modifiedBy;

    // Equals and HashCode --------------------------------
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
