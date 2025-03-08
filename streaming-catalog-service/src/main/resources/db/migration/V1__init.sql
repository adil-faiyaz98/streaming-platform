-- src/main/resources/db/migration/V1__init.sql
CREATE TABLE movies (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        release_year INTEGER,
                        director VARCHAR(255),
                        duration INTEGER,
                        maturity_rating VARCHAR(50),
                        image_url VARCHAR(255),
                        video_url VARCHAR(255),
                        average_rating DOUBLE PRECISION DEFAULT 0.0,
                        view_count BIGINT DEFAULT 0,
                        featured BOOLEAN DEFAULT FALSE,
                        created_at TIMESTAMP WITH TIME ZONE,
                        updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE movie_genres (
                              movie_id BIGINT REFERENCES movies(id),
                              genres VARCHAR(50),
                              PRIMARY KEY (movie_id, genres)
);

-- Add indexes
CREATE INDEX idx_movies_title ON movies(title);
CREATE INDEX idx_movies_release_year ON movies(release_year);
CREATE INDEX idx_movies_featured ON movies(featured);