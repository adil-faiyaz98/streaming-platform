package com.examples.streaming_platform.recommendation.repository;

import com.examples.streaming_platform.recommendation.model.ContentItem;
import com.examples.streaming_platform.recommendation.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository for accessing and manipulating ContentItem entities.
 */
@Repository
public interface ContentItemRepository extends JpaRepository<ContentItem, String> {

    /**
     * Find content items by item type.
     *
     * @param itemType the item type
     * @return a list of content items
     */
    List<ContentItem> findByItemType(UserInteraction.ItemType itemType);

    /**
     * Find content items by genre.
     *
     * @param genre the genre
     * @return a list of content items
     */
    @Query("SELECT ci FROM ContentItem ci JOIN ci.genres g WHERE g = :genre")
    List<ContentItem> findByGenre(@Param("genre") String genre);

    /**
     * Find content items by multiple genres (items that have ANY of the specified genres).
     *
     * @param genres the set of genres
     * @return a list of content items
     */
    @Query("SELECT DISTINCT ci FROM ContentItem ci JOIN ci.genres g WHERE g IN :genres")
    List<ContentItem> findByGenresIn(@Param("genres") Set<String> genres);

    /**
     * Find content items by multiple genres (items that have ALL of the specified genres).
     *
     * @param genres the set of genres
     * @return a list of content items
     */
    @Query("SELECT ci FROM ContentItem ci WHERE SIZE(ci.genres) >= :genreCount AND " +
           "(SELECT COUNT(g) FROM ci.genres g WHERE g IN :genres) = :genreCount")
    List<ContentItem> findByAllGenres(
            @Param("genres") Set<String> genres,
            @Param("genreCount") int genreCount);

    /**
     * Find content items by actor.
     *
     * @param actor the actor name
     * @return a list of content items
     */
    @Query("SELECT ci FROM ContentItem ci JOIN ci.actors a WHERE a = :actor")
    List<ContentItem> findByActor(@Param("actor") String actor);

    /**
     * Find content items by release year range.
     *
     * @param startYear the start year (inclusive)
     * @param endYear the end year (inclusive)
     * @return a list of content items
     */
    List<ContentItem> findByReleaseYearBetween(int startYear, int endYear);

    /**
     * Find content items by minimum rating.
     *
     * @param minRating the minimum rating
     * @return a list of content items
     */
    List<ContentItem> findByAverageRatingGreaterThanEqual(double minRating);

    /**
     * Find similar content items based on genre overlap.
     *
     * @param itemId the reference item ID
     * @param minGenreOverlap the minimum number of overlapping genres
     * @param limit the maximum number of items to return
     * @return a list of similar content items
     */
    @Query(value = "WITH item_genres AS (" +
            "  SELECT genre FROM content_item_genres WHERE item_id = :itemId" +
            "), similar_items AS (" +
            "  SELECT ci.item_id, COUNT(cig.genre) AS overlap_count" +
            "  FROM content_items ci" +
            "  JOIN content_item_genres cig ON ci.item_id = cig.item_id" +
            "  WHERE cig.genre IN (SELECT genre FROM item_genres)" +
            "  AND ci.item_id != :itemId" +
            "  GROUP BY ci.item_id" +
            "  HAVING COUNT(cig.genre) >= :minGenreOverlap" +
            ")" +
            "SELECT ci.* FROM content_items ci" +
            " JOIN similar_items si ON ci.item_id = si.item_id" +
            " ORDER BY si.overlap_count DESC, ci.popularity_score DESC" +
            " LIMIT :limit",
            nativeQuery = true)
    List<ContentItem> findSimilarContentByGenres(
            @Param("itemId") String itemId,
            @Param("minGenreOverlap") int minGenreOverlap,
            @Param("limit") int limit);

    /**
     * Find the most popular content items.
     *
     * @param limit the maximum number of items to return
     * @return a list of popular content items
     */
    List<ContentItem> findByOrderByPopularityScoreDesc(int limit);
}
