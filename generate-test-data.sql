-- Create test users for streaming platform
-- Note: Passwords are BCrypt hashed
-- admin/admin123
-- user/user123

-- For catalog service database
\c streaming_platform_db;

-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create user_roles table if it doesn't exist
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert admin user
INSERT INTO users (username, password, email, full_name)
VALUES ('admin', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'admin@example.com', 'Admin User')
ON CONFLICT (username) DO NOTHING;

-- Insert regular user
INSERT INTO users (username, password, email, full_name)
VALUES ('user', '$2a$10$Eg2fJeOgL2VyBBDo7W9G5O3ZYXdLTEMxnIKBpIXL/c9/Ay1YPRpOi', 'user@example.com', 'Regular User')
ON CONFLICT (username) DO NOTHING;

-- Insert roles for admin
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER' FROM users WHERE username = 'admin'
ON CONFLICT DO NOTHING;

-- Insert roles for regular user
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER' FROM users WHERE username = 'user'
ON CONFLICT DO NOTHING;

-- For recommendation service database
\c recommendation_db;

-- Create sample movies if they don't exist in catalog service
\c streaming_platform_db;

-- Create movies table if it doesn't exist
CREATE TABLE IF NOT EXISTS movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_year INTEGER,
    director VARCHAR(255),
    duration INTEGER,
    maturity_rating VARCHAR(50),
    image_url VARCHAR(255),
    average_rating DOUBLE PRECISION DEFAULT 0.0,
    view_count BIGINT DEFAULT 0,
    featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create movie_genres table if it doesn't exist
CREATE TABLE IF NOT EXISTS movie_genres (
    movie_id BIGINT NOT NULL,
    genres VARCHAR(50) NOT NULL,
    PRIMARY KEY (movie_id, genres),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Insert sample movies
INSERT INTO movies (title, description, release_year, director, duration, maturity_rating, image_url, average_rating, view_count, featured)
VALUES 
('Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology.', 2010, 'Christopher Nolan', 148, 'PG-13', 'https://example.com/inception.jpg', 4.8, 1000000, true),
('The Shawshank Redemption', 'Two imprisoned men bond over a number of years.', 1994, 'Frank Darabont', 142, 'R', 'https://example.com/shawshank.jpg', 4.9, 900000, true),
('The Godfather', 'The aging patriarch of an organized crime dynasty transfers control to his son.', 1972, 'Francis Ford Coppola', 175, 'R', 'https://example.com/godfather.jpg', 4.8, 800000, true),
('The Dark Knight', 'Batman fights the menace known as the Joker.', 2008, 'Christopher Nolan', 152, 'PG-13', 'https://example.com/darkknight.jpg', 4.7, 950000, true),
('Pulp Fiction', 'The lives of two mob hitmen, a boxer, a gangster and his wife intertwine.', 1994, 'Quentin Tarantino', 154, 'R', 'https://example.com/pulpfiction.jpg', 4.6, 850000, false),
('Fight Club', 'An insomniac office worker and a devil-may-care soapmaker form an underground fight club.', 1999, 'David Fincher', 139, 'R', 'https://example.com/fightclub.jpg', 4.5, 750000, false),
('Forrest Gump', 'The presidencies of Kennedy and Johnson, the Vietnam War, and Watergate through the eyes of an Alabama man.', 1994, 'Robert Zemeckis', 142, 'PG-13', 'https://example.com/forrestgump.jpg', 4.7, 900000, false),
('The Matrix', 'A computer hacker learns about the true nature of reality.', 1999, 'Lana and Lilly Wachowski', 136, 'R', 'https://example.com/matrix.jpg', 4.6, 850000, true),
('Goodfellas', 'The story of Henry Hill and his life in the mob.', 1990, 'Martin Scorsese', 146, 'R', 'https://example.com/goodfellas.jpg', 4.5, 700000, false),
('The Silence of the Lambs', 'A young FBI cadet must receive the help of an incarcerated and manipulative cannibal killer.', 1991, 'Jonathan Demme', 118, 'R', 'https://example.com/silenceofthelambs.jpg', 4.4, 650000, false)
ON CONFLICT DO NOTHING;

-- Insert movie genres
INSERT INTO movie_genres (movie_id, genres)
VALUES 
(1, 'ACTION'), (1, 'SCIENCE_FICTION'), (1, 'THRILLER'),
(2, 'DRAMA'),
(3, 'CRIME'), (3, 'DRAMA'),
(4, 'ACTION'), (4, 'CRIME'), (4, 'DRAMA'),
(5, 'CRIME'), (5, 'DRAMA'),
(6, 'DRAMA'), (6, 'THRILLER'),
(7, 'DRAMA'), (7, 'ROMANCE'),
(8, 'ACTION'), (8, 'SCIENCE_FICTION'),
(9, 'CRIME'), (9, 'DRAMA'),
(10, 'CRIME'), (10, 'THRILLER'), (10, 'HORROR')
ON CONFLICT DO NOTHING;
