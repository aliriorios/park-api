package br.com.example.park_api.entity;

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
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ToString.Include
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @ToString.Include
    private String name;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

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
        Client client = (Client) object;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
