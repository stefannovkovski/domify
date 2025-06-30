package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "landlordprofile", schema = "domify")
@Data
@NoArgsConstructor
public class LandlordProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Column(name = "managed_properties_count", nullable = false)
    private Integer managedPropertiesCount = 0;

    @Column(name = "is_agent", nullable = false)
    private Boolean isAgent;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_landlord_user"))
    private UserD user;

    public LandlordProfile(Integer managedPropertiesCount, Boolean isAgent, UserD user) {
        this.managedPropertiesCount = managedPropertiesCount;
        this.isAgent = isAgent;
        this.user = user;
    }
}