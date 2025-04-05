package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by username.
     *
     * @param username the username
     * @return an optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     *
     * @param email the email
     * @return an optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by username.
     *
     * @param username the username
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if a user exists by email.
     *
     * @param email the email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
