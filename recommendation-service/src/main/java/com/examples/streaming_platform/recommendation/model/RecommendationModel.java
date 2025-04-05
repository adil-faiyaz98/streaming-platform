package com.examples.streaming_platform.recommendation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Entity representing a trained recommendation model.
 * This stores metadata about the model and its performance metrics.
 */
@Entity
@Table(name = "recommendation_models")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "training_duration_ms")
    private Long trainingDurationMs;

    @Column(name = "data_points_count")
    private Integer dataPointsCount;

    @Column(name = "precision_metric")
    private Double precisionMetric;

    @Column(name = "recall_metric")
    private Double recallMetric;

    @Column(name = "f1_score")
    private Double f1Score;

    @Column(name = "mean_average_precision")
    private Double meanAveragePrecision;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "parameters", columnDefinition = "jsonb")
    private String parameters;

    /**
     * Type of recommendation model.
     */
    public enum ModelType {
        COLLABORATIVE_FILTERING,
        CONTENT_BASED,
        HYBRID,
        NEURAL_NETWORK,
        MATRIX_FACTORIZATION,
        POPULARITY_BASED
    }
}
