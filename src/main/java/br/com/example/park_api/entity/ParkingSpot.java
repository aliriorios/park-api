package br.com.example.park_api.entity;

import br.com.example.park_api.entity.enums.SpotStatus;
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
@Table(name = "parking_spots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ParkingSpot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ToString.Include
    private Long id;

    @Column(name = "spot_code", nullable = false, unique = true, length = 4)
    @ToString.Include
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @ToString.Include
    private SpotStatus status;

    // Auditor Aware fields -------------------------------
    @Column(name = "created_date")
    @Setter(AccessLevel.NONE)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @Setter(AccessLevel.NONE)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "created_by")
    @Setter(AccessLevel.NONE)
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @Setter(AccessLevel.NONE)
    @LastModifiedBy
    private String modifiedBy;

    // Equals and HashCode --------------------------------
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ParkingSpot that = (ParkingSpot) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
