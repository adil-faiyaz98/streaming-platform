package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.model.Genre;
import com.examples.streaming_platform.catalog.model.Movie;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.util.HashSet;
import java.util.Set;

public class MovieSpecification implements Specification<Movie> {

    private final String director;
    private final String language;
    private final String countryOfOrigin;
    private final Integer minDuration;
    private final Integer maxDuration;
    private final Set<Genre> genres;

    public MovieSpecification(String director, String language, String countryOfOrigin,
                              Integer minDuration, Integer maxDuration,
                              Set<Genre> genres) {
        this.director = director;
        this.language = language;
        this.countryOfOrigin = countryOfOrigin;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.genres = (genres != null) ? genres : new HashSet<>();
    }

    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var predicates = new java.util.ArrayList<Predicate>();

        if (director != null && !director.isEmpty()) {
            predicates.add(cb.equal(cb.lower(root.get("director")), director.toLowerCase()));
        }
        if (language != null && !language.isEmpty()) {
            predicates.add(cb.equal(cb.lower(root.get("language")), language.toLowerCase()));
        }
        if (countryOfOrigin != null && !countryOfOrigin.isEmpty()) {
            predicates.add(cb.equal(cb.lower(root.get("countryOfOrigin")), countryOfOrigin.toLowerCase()));
        }
        if (minDuration != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), minDuration));
        }
        if (maxDuration != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("duration"), maxDuration));
        }
        if (!genres.isEmpty()) {
            Join<Object, Object> genreJoin = root.join("genres", JoinType.INNER);
            predicates.add(genreJoin.in(genres));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
