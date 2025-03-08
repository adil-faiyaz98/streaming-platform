// model/Season.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;

    private Integer seasonNumber;
    private String title;
    private String description;
    private Integer releaseYear;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private Set<Episode> episodes;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}