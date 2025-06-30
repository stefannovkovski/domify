package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentd", schema = "domify")
@Data
@NoArgsConstructor
public class DocumentD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "file_type", nullable = false, length = 50)
    private String fileType;

    @NotBlank
    @Size(max = 500)
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_document_user"))
    private UserD user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_id", foreignKey = @ForeignKey(name = "fk_document_lease"))
    private Lease lease;

    public DocumentD(String fileType, String fileUrl, LocalDateTime uploadedAt, UserD user, Lease lease) {
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
        this.user = user;
        this.lease = lease;
    }
}