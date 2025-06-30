package com.example.domify.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tenantprofile", schema = "domify")
@Data
@NoArgsConstructor
public class TenantProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_tenant_user"))
    private UserD user;

    public TenantProfile(UserD user) {
        this.user = user;
    }
}