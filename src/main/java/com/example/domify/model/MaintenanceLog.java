package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "maintenancelog", schema = "domify")
@Data
@NoArgsConstructor
public class MaintenanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_request_id", nullable = false, foreignKey = @ForeignKey(name = "fk_maintenancelog_servicerequest"))
    private ServiceRequest serviceRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_maintenancelog_user"))
    private UserD user;

    public MaintenanceLog(String description, LocalDate maintenanceDate, ServiceRequest serviceRequest, UserD user) {
        this.description = description;
        this.maintenanceDate = maintenanceDate;
        this.serviceRequest = serviceRequest;
        this.user = user;
    }
}