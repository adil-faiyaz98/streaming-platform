-- src/main/resources/db/migration/V2__add_missing_tables_and_indexes.sql

-- Create tv_shows table
CREATE TABLE tv_shows (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description VARCHAR(2000),
                          first_air_date DATE,
                          rating DOUBLE PRECISION DEFAULT 0.0,
                          poster_url VARCHAR(512),
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE
);

-- Create tv_show_genres table
CREATE TABLE tv_show_genres (
                                tv_show_id BIGINT REFERENCES tv_shows(id) ON DELETE CASCADE,
                                genre VARCHAR(50) NOT NULL,
                                PRIMARY KEY (tv_show_id, genre)
);

-- Add missing columns to seasons
ALTER TABLE seasons
    ADD COLUMN IF NOT EXISTS tv_show_id BIGINT REFERENCES tv_shows(id) ON DELETE CASCADE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    ADD COLUMN IF NOT EXISTS season_number INTEGER NOT NULL;

-- Correct existing column definitions if missing
ALTER TABLE series
    ADD COLUMN IF NOT EXISTS start_year INTEGER;

ALTER TABLE episodes
    ADD COLUMN IF NOT EXISTS duration INTEGER;

-- INDEXES --
-- Movies Indexes
CREATE INDEX IF NOT EXISTS idx_movies_title ON movies(title);
CREATE INDEX IF NOT EXISTS idx_movies_featured ON movies(featured);
CREATE INDEX IF NOT EXISTS idx_movies_created_at ON movies(created_at);

-- SeriesController Indexes
CREATE INDEX IF NOT EXISTS idx_series_title ON series(title);
CREATE INDEX IF NOT EXISTS idx_series_featured ON series(featured);
CREATE INDEX IF NOT EXISTS idx_series_start_year ON series(start_year);

-- TV Shows Indexes
CREATE INDEX IF NOT EXISTS idx_tv_shows_title ON tv_shows(title);
CREATE INDEX IF NOT EXISTS idx_tv_shows_first_air_date ON tv_shows(first_air_date);

-- Seasons Indexes
CREATE INDEX IF NOT EXISTS idx_seasons_tv_show_id ON seasons(tv_show_id);
CREATE INDEX IF NOT EXISTS idx_seasons_series_id ON seasons(series_id);

-- Episodes Indexes
CREATE INDEX IF NOT EXISTS idx_episodes_season_id ON episodes(season_id);
CREATE INDEX IF NOT EXISTS idx_episodes_episode_number ON episodes(episode_number);
