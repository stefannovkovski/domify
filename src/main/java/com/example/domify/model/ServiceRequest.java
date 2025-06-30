package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "servicerequest", schema = "domify")
@Data
@NoArgsConstructor
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_id", nullable = false, foreignKey = @ForeignKey(name = "fk_servicerequest_lease"))
    private Lease lease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_servicerequest_category"))
    private ServiceCategory serviceCategory;

    public ServiceRequest(String description, LocalDate requestDate, String status, Lease lease, ServiceCategory serviceCategory) {
        this.description = description;
        this.requestDate = requestDate;
        this.status = status;
        this.lease = lease;
        this.serviceCategory = serviceCategory;
    }
}