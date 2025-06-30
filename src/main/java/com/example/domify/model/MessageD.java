package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messaged", schema = "domify")
@Data
@NoArgsConstructor
public class MessageD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_from_user"))
    private UserD fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_to_user"))
    private UserD toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_lease"))
    private Lease lease;

    public MessageD(String content, LocalDateTime sentAt, UserD fromUser, UserD toUser, Lease lease) {
        this.content = content;
        this.sentAt = sentAt;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.lease = lease;
    }
}