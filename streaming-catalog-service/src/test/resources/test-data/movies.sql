-- Test data for movies
INSERT INTO movies (id, title, description, release_year, director, duration, maturity_rating, image_url, average_rating, view_count, featured)
VALUES 
(1, 'Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology.', 2010, 'Christopher Nolan', 148, 'PG-13', 'https://example.com/inception.jpg', 4.8, 1000000, true),
(2, 'The Shawshank Redemption', 'Two imprisoned men bond over a number of years.', 1994, 'Frank Darabont', 142, 'R', 'https://example.com/shawshank.jpg', 4.9, 900000, true),
(3, 'The Godfather', 'The aging patriarch of an organized crime dynasty transfers control to his son.', 1972, 'Francis Ford Coppola', 175, 'R', 'https://example.com/godfather.jpg', 4.8, 800000, true),
(4, 'The Dark Knight', 'Batman fights the menace known as the Joker.', 2008, 'Christopher Nolan', 152, 'PG-13', 'https://example.com/darkknight.jpg', 4.7, 950000, true),
(5, 'Pulp Fiction', 'The lives of two mob hitmen, a boxer, a gangster and his wife intertwine.', 1994, 'Quentin Tarantino', 154, 'R', 'https://example.com/pulpfiction.jpg', 4.6, 850000, false),
(6, 'Fight Club', 'An insomniac office worker and a devil-may-care soapmaker form an underground fight club.', 1999, 'David Fincher', 139, 'R', 'https://example.com/fightclub.jpg', 4.5, 750000, false),
(7, 'Forrest Gump', 'The presidencies of Kennedy and Johnson, the Vietnam War, and Watergate through the eyes of an Alabama man.', 1994, 'Robert Zemeckis', 142, 'PG-13', 'https://example.com/forrestgump.jpg', 4.7, 900000, false),
(8, 'The Matrix', 'A computer hacker learns about the true nature of reality.', 1999, 'Lana and Lilly Wachowski', 136, 'R', 'https://example.com/matrix.jpg', 4.6, 850000, true),
(9, 'Goodfellas', 'The story of Henry Hill and his life in the mob.', 1990, 'Martin Scorsese', 146, 'R', 'https://example.com/goodfellas.jpg', 4.5, 700000, false),
(10, 'The Silence of the Lambs', 'A young FBI cadet must receive the help of an incarcerated and manipulative cannibal killer.', 1991, 'Jonathan Demme', 118, 'R', 'https://example.com/silenceofthelambs.jpg', 4.4, 650000, false);

-- Insert movie genres
INSERT INTO movie_genres (movie_id, genres) VALUES 
(1, 'ACTION'), (1, 'SCIENCE_FICTION'), (1, 'THRILLER'),
(2, 'DRAMA'),
(3, 'CRIME'), (3, 'DRAMA'),
(4, 'ACTION'), (4, 'CRIME'), (4, 'DRAMA'),
(5, 'CRIME'), (5, 'DRAMA'),
(6, 'DRAMA'), (6, 'THRILLER'),
(7, 'DRAMA'), (7, 'ROMANCE'),
(8, 'ACTION'), (8, 'SCIENCE_FICTION'),
(9, 'CRIME'), (9, 'DRAMA'),
(10, 'CRIME'), (10, 'THRILLER'), (10, 'HORROR');
