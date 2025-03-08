// model/Series.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "series", indexes = {
        @Index(name = "idx_series_title", columnList = "title"),
        @Index(name = "idx_series_featured", columnList = "featured")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Series {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "start_year")
    private Integer startYear;
    
    @Column(name = "end_year")
    private Integer endYear;
    
    @Column(name = "maturity_rating")
    private String maturityRating;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "average_rating")
    private Double averageRating = 0.0;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    private Boolean featured = false;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    
    @ElementCollection
    @CollectionTable(name = "series_genres", joinColumns = @JoinColumn(name = "series_id"))
    @Column(name = "genres")
    private Set<String> genres = new HashSet<>();
    
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Season> seasons = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}