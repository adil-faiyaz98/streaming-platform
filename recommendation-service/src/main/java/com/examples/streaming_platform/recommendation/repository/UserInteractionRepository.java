package com.examples.streaming_platform.recommendation.repository;

import com.examples.streaming_platform.recommendation.model.UserInteraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing and manipulating UserInteraction entities.
 */
@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    /**
     * Find interactions by user ID.
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return a page of user interactions
     */
    Page<UserInteraction> findByUserId(String userId, Pageable pageable);

    /**
     * Find interactions by item ID.
     *
     * @param itemId the item ID
     * @param pageable pagination information
     * @return a page of user interactions
     */
    Page<UserInteraction> findByItemId(String itemId, Pageable pageable);

    /**
     * Find interactions by user ID and interaction type.
     *
     * @param userId the user ID
     * @param interactionType the interaction type
     * @param pageable pagination information
     * @return a page of user interactions
     */
    Page<UserInteraction> findByUserIdAndInteractionType(
            String userId, 
            UserInteraction.InteractionType interactionType, 
            Pageable pageable);

    /**
     * Find the most recent interaction between a user and an item.
     *
     * @param userId the user ID
     * @param itemId the item ID
     * @return the most recent interaction, if any
     */
    Optional<UserInteraction> findTopByUserIdAndItemIdOrderByTimestampDesc(String userId, String itemId);

    /**
     * Find all interactions for a user within a time range.
     *
     * @param userId the user ID
     * @param startTime the start time
     * @param endTime the end time
     * @return a list of user interactions
     */
    List<UserInteraction> findByUserIdAndTimestampBetween(
            String userId, 
            OffsetDateTime startTime, 
            OffsetDateTime endTime);

    /**
     * Find users who interacted with the same items as the specified user.
     *
     * @param userId the user ID
     * @param interactionType the interaction type
     * @param minInteractions minimum number of common interactions
     * @return a list of user IDs with similar interactions
     */
    @Query(value = "SELECT ui2.user_id FROM user_interactions ui1 " +
            "JOIN user_interactions ui2 ON ui1.item_id = ui2.item_id " +
            "WHERE ui1.user_id = :userId " +
            "AND ui1.interaction_type = :interactionType " +
            "AND ui2.interaction_type = :interactionType " +
            "AND ui2.user_id != :userId " +
            "GROUP BY ui2.user_id " +
            "HAVING COUNT(DISTINCT ui1.item_id) >= :minInteractions",
            nativeQuery = true)
    List<String> findSimilarUsers(
            @Param("userId") String userId,
            @Param("interactionType") String interactionType,
            @Param("minInteractions") int minInteractions);

    /**
     * Find items that are frequently interacted with together with the specified item.
     *
     * @param itemId the item ID
     * @param interactionType the interaction type
     * @param minCooccurrences minimum number of co-occurrences
     * @return a list of item IDs that frequently co-occur
     */
    @Query(value = "SELECT ui2.item_id FROM user_interactions ui1 " +
            "JOIN user_interactions ui2 ON ui1.user_id = ui2.user_id " +
            "WHERE ui1.item_id = :itemId " +
            "AND ui1.interaction_type = :interactionType " +
            "AND ui2.interaction_type = :interactionType " +
            "AND ui2.item_id != :itemId " +
            "GROUP BY ui2.item_id " +
            "HAVING COUNT(DISTINCT ui1.user_id) >= :minCooccurrences " +
            "ORDER BY COUNT(DISTINCT ui1.user_id) DESC",
            nativeQuery = true)
    List<String> findCooccurringItems(
            @Param("itemId") String itemId,
            @Param("interactionType") String interactionType,
            @Param("minCooccurrences") int minCooccurrences);

    /**
     * Find trending items based on recent interaction counts.
     *
     * @param interactionTypes the interaction types to consider
     * @param since the start time for trending calculation
     * @param limit the maximum number of items to return
     * @return a list of trending item IDs
     */
    @Query(value = "SELECT item_id FROM user_interactions " +
            "WHERE interaction_type IN :interactionTypes " +
            "AND timestamp >= :since " +
            "GROUP BY item_id " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<String> findTrendingItems(
            @Param("interactionTypes") List<String> interactionTypes,
            @Param("since") OffsetDateTime since,
            @Param("limit") int limit);
}
