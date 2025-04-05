package com.examples.streaming_platform.recommendation.repository;

import com.examples.streaming_platform.recommendation.model.RecommendationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing and manipulating RecommendationModel entities.
 */
@Repository
public interface RecommendationModelRepository extends JpaRepository<RecommendationModel, Long> {

    /**
     * Find the active model of a specific type.
     *
     * @param modelType the model type
     * @return the active model, if any
     */
    Optional<RecommendationModel> findByModelTypeAndIsActiveTrue(RecommendationModel.ModelType modelType);

    /**
     * Find models by type, ordered by creation time.
     *
     * @param modelType the model type
     * @return a list of models
     */
    List<RecommendationModel> findByModelTypeOrderByCreatedAtDesc(RecommendationModel.ModelType modelType);

    /**
     * Find the most recent model of a specific type.
     *
     * @param modelType the model type
     * @return the most recent model, if any
     */
    Optional<RecommendationModel> findTopByModelTypeOrderByCreatedAtDesc(RecommendationModel.ModelType modelType);

    /**
     * Find models by version.
     *
     * @param version the version
     * @return a list of models
     */
    List<RecommendationModel> findByVersion(String version);
}
