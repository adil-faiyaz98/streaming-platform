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
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    genres VARCHAR(50),
    PRIMARY KEY (movie_id, genres)
);

CREATE TABLE series (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_year INTEGER,
    end_year INTEGER,
    maturity_rating VARCHAR(50),
    image_url VARCHAR(255),
    average_rating DOUBLE PRECISION DEFAULT 0.0,
    view_count BIGINT DEFAULT 0,
    featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE series_genres (
    series_id BIGINT REFERENCES series(id) ON DELETE CASCADE,
    genres VARCHAR(50),
    PRIMARY KEY (series_id, genres)
);

CREATE TABLE seasons (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT REFERENCES series(id) ON DELETE CASCADE,
    season_number INTEGER NOT NULL,
    title VARCHAR(255),
    description TEXT,
    release_year INTEGER,
    image_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT REFERENCES seasons(id) ON DELETE CASCADE,
    episode_number INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration INTEGER,
    image_url VARCHAR(255),
    video_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Add indexes
CREATE INDEX idx_movies_title ON movies(title);
CREATE INDEX idx_movies_release_year ON movies(release_year);
CREATE INDEX idx_movies_featured ON movies(featured);
CREATE INDEX idx_series_title ON series(title);
CREATE INDEX idx_series_start_year ON series(start_year);
CREATE INDEX idx_series_featured ON series(featured);
CREATE INDEX idx_seasons_series_id ON seasons(series_id);
CREATE INDEX idx_episodes_season_id ON episodes(season_id);