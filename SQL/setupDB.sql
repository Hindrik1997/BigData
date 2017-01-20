-- start Hindrik

CREATE SCHEMA staging;

-- Table: staging.actors

-- DROP TABLE staging.actors;

CREATE TABLE staging.actors
(
  actor_name TEXT,
  movie_title TEXT,
  serie_title TEXT,
  year_of_release INTEGER,
  quarter TEXT,
  state TEXT,
  episode_date TEXT,
  episode_name TEXT,
  season_nr INTEGER,
  episode_nr INTEGER,
  platform TEXT,
  voice TEXT,
  credited_as TEXT,
  character_name TEXT,
  billing_position INTEGER,
  gender TEXT
)
WITH (
  OIDS=FALSE
);
ALTER TABLE staging.actors
  OWNER TO postgres;

-- Table: staging.movies

-- DROP TABLE staging.movies;

CREATE TABLE staging.movies
(
    title TEXT  NOT NULL,
	  year_of_release INTEGER,
    quarter TEXT ,
    platform TEXT ,
    state TEXT
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE staging.movies
    OWNER to postgres;


-- Table: staging.series

-- DROP TABLE staging.series;

CREATE TABLE staging.series
(
    title TEXT  NOT NULL,
    serie_started INTEGER,
    quarter TEXT ,
    episode_name TEXT ,
    episode_date TEXT ,
    season_nr INTEGER,
    episode_nr INTEGER,
    episode_year INTEGER,
    state TEXT
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE staging.series
    OWNER to postgres;

-- Table: staging.biografies

-- DROP TABLE staging.biografies;

CREATE TABLE staging.biografies
(
    name TEXT ,
    real_name TEXT ,
    height TEXT ,
    birthdate TEXT ,
    birth_location TEXT ,
    deathdate TEXT ,
    death_location TEXT ,
    cause_of_death TEXT
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE staging.biografies
    OWNER to postgres;

-- Table: staging.genres

-- DROP TABLE staging.genres;

CREATE TABLE staging.genres
(
    movie TEXT ,
    serie TEXT ,
    year_of_release INTEGER,
    quarter TEXT ,
    state TEXT,
    episode_date TEXT,
    episode_name TEXT,
    season_nr INTEGER,
    episode_nr INTEGER,
    platform TEXT ,
    genre TEXT NOT NULL
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE staging.genres
    OWNER to postgres;

-- Table: staging.location_series

-- DROP TABLE staging.location_series;

CREATE TABLE staging.location_series
(
  title TEXT,
  year_of_release INTEGER,
  quarter TEXT,
  episode_name TEXT,
  season_nr INTEGER,
  episode_nr INTEGER,
  location TEXT
)
WITH (
  OIDS=FALSE
);
ALTER TABLE staging.location_series
  OWNER TO postgres;

-- Table: staging.location_movies
-- DROP TABLE staging.location_movies;

CREATE TABLE staging.location_movies
(
  title TEXT,
  year_of_release INTEGER,
  quarter TEXT,
  platform TEXT,
  state TEXT,
  location TEXT
)
WITH (
  OIDS=FALSE
);
ALTER TABLE staging.location_movies
  OWNER TO postgres;

CREATE TABLE staging.rating_movies
(
  title TEXT,
  year_of_release INTEGER,
  quarter TEXT,
  platform TEXT,
  rating_major INTEGER,
  rating_minor INTEGER,
  voters INTEGER
) WITH (OIDS = FALSE);

ALTER TABLE staging.rating_movies OWNER TO postgres;

CREATE TABLE staging.rating_series
(
  title TEXT,
  year_of_release INTEGER,
  quarter TEXT,
  episode_name TEXT,
  season_nr INTEGER,
  episode_nr INTEGER,
  rating_major INTEGER,
  rating_minor INTEGER,
  voters INTEGER
) WITH (OIDS = FALSE);

ALTER TABLE staging.rating_series OWNER TO postgres;

COPY staging.rating_series
(title, year_of_release, quarter, episode_name, season_nr, episode_nr, rating_major, rating_minor, voters)
FROM
'/tmp/series_ratings.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.rating_movies
(title, year_of_release, quarter, platform, rating_major, rating_minor, voters)
FROM
'/tmp/movies_ratings.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.actors (
  actor_name,
  movie_title,
  serie_title,
  year_of_release,
  quarter,
  state,
  episode_date,
  episode_name,
  season_nr, episode_nr,
  platform,
  voice,
  credited_as,
  character_name,
  billing_position,
  gender
)
FROM
'/tmp/actors.csv'
DELIMITER AS '|' NULL '' ENCODING 'ISO88591';

COPY staging.biografies(
  name, real_name, height, birthdate, birth_location, deathdate, death_location, cause_of_death)
FROM
'/tmp/biography.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.movies(
  title, year_of_release, quarter, platform, state)
FROM
'/tmp/movies.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.series(
  title, serie_started, quarter, episode_name, episode_date, season_nr, episode_nr, episode_year, state)
FROM
'/tmp/series.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.location_series(
    title, year_of_release, quarter, episode_name, season_nr, episode_nr, location
)   FROM
'/tmp/locationseries.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.location_movies(
  title, year_of_release, quarter, platform, state, location)
FROM
'/tmp/locationmovies.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';

COPY staging.genres(
  movie, serie, year_of_release, quarter, state, episode_date, episode_name, season_nr, episode_nr, platform, genre)
FROM
'/tmp/genre.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO88591';


CREATE INDEX title_movies_idx ON staging.movies(title);
CREATE INDEX title_series_idx ON staging.series(title);

CREATE INDEX title_movie_genres_idx ON staging.genres(movie);
CREATE INDEX title_serie_genres_idx ON staging.genres(serie);

CREATE INDEX title_movie_location_idx ON staging.location_movies(title);
CREATE INDEX title_serie_location_idx ON staging.location_series(title);

CREATE INDEX title_serie_ratings_idx ON staging.rating_series(title);
CREATE INDEX title_movie_ratings_idx ON staging.rating_movies(title);

CREATE SCHEMA final;

CREATE TABLE final.actors(

  actor_id SERIAL,
  actor_name TEXT,
  real_name TEXT,
  height TEXT,
  birth_date TEXT,
  birth_location TEXT,
  death_date TEXT,
  death_location TEXT,
  cause_of_death TEXT,
  gender TEXT,
  PRIMARY KEY (actor_id)
)
WITH (OIDS = FALSE);

ALTER TABLE final.actors OWNER TO postgres;

INSERT INTO final.actors(actor_name, gender) SELECT DISTINCT actor_name, gender FROM staging.actors;

UPDATE final.actors
SET
  real_name = staging.biografies.real_name,
  height = staging.biografies.height,
  birth_date = staging.biografies.birthdate,
  birth_location = staging.biografies.birth_location,
  death_date = staging.biografies.deathdate,
  death_location = staging.biografies.death_location,
  cause_of_death = staging.biografies.cause_of_death
FROM staging.biografies
WHERE final.actors.actor_name = staging.biografies.name;
--einde Hindrik
-- start Romeo
CREATE TABLE staging.location_staging(
  location TEXT
);

CREATE INDEX movie_location_idx ON staging.location_movies(location);
CREATE INDEX serie_location_idx ON staging.location_series(location);

INSERT INTO staging.location_staging (location)
SELECT location FROM staging.location_movies;

INSERT INTO staging.location_staging (location)
SELECT location FROM staging.location_series;

CREATE TABLE final.location(
  location_id SERIAL UNIQUE NOT NULL,
  location TEXT,
  PRIMARY KEY (location_id)
);

INSERT INTO final.location (location)
SELECT DISTINCT location FROM staging.location_staging;

DROP TABLE staging.location_staging;
-- einde Romeo

-- start Jacob
CREATE TABLE final.genre(
 genre_id SERIAL UNIQUE NOT NULL,
 genre TEXT,
 PRIMARY KEY (genre_id)
 );

INSERT INTO final.genre(genre)
SELECT DISTINCT genre from staging.genres;
-- einde Jacob
-- start Denny
CREATE TABLE final.movie(
movie_id SERIAL UNIQUE NOT NULL,
title text,
year_of_release integer,
quarter text,
platform text,
state text,
rating_major integer,
rating_minor integer,
voters integer
);

INSERT INTO final.movie(
title, year_of_release, quarter, platform, state)
SELECT title, year_of_release, quarter, platform, state
FROM staging.movies;

-- einde denny
-- start Romeo
CREATE TABLE final.movie_location (
	movie_id int not null,
	location_id int not null,
  PRIMARY KEY (movie_id,location_id),
  CONSTRAINT fk_movie_location_movie_id
		FOREIGN KEY (movie_id)
		REFERENCES final.movie (movie_id),
	CONSTRAINT fk_movie_location_location_id
		FOREIGN KEY (location_id)
		REFERENCES final.location ("location_id")
);

INSERT INTO final.movie_location (movie_id, location_id)
SELECT M.movie_id, L.location_id FROM final.movie M
INNER JOIN staging.location_movies ML ON M.title=ML.title
	AND M.year_of_release=ML.year_of_release OR M.year_of_release= NULL AND ML.year_of_release=NULL AND M.quarter=ML.quarter OR M.quarter=NULL AND ML.quarter=NULL
INNER JOIN final.location L ON ML.location=L.location
ON CONFLICT DO NOTHING;
-- einde Romeo

-- start Jacob
CREATE TABLE final.movie_genre (
	movie_id SERIAl NOT NULL,
	genre_id SERIAL NOT NULL,
	CONSTRAINT "fk_movie_genre_movie_id"
 		FOREIGN KEY (movie_id)
		REFERENCES final.movie(movie_id),
 	CONSTRAINT "fk_movie_genre_genre_id"
 		FOREIGN KEY (genre_id)
		REFERENCES final.genre(genre_id),
	PRIMARY KEY (movie_id,genre_id)
);

INSERT INTO final.movie_genre(movie_id, genre_id)
SELECT M.movie_id, G.genre_id FROM final.movie M
INNER JOIN staging.genres SG ON M.title=SG.movie
	AND M.year_of_release=SG.year_of_release OR M.year_of_release= NULL AND SG.year_of_release=NULL AND M.quarter=SG.quarter OR M.quarter=NULL AND SG.quarter=NULL
INNER JOIN final.genre G ON SG.genre=G.genre
ON CONFLICT DO NOTHING;
-- einde Jacob
-- start Denny


UPDATE final.movie M
SET rating_major = R.rating_major,
rating_minor = R.rating_minor,
voters = R.voters
FROM staging.rating_movies R
WHERE M.title = R.title AND
(M.year_of_release = R.year_of_release OR
 (M.year_of_release IS NULL AND R.year_of_release IS NULL))
 AND (M.quarter = R.quarter OR
      (M.quarter IS NULL AND R.quarter IS NULL))
AND R.rating_major IS NOT NULL AND R.rating_minor IS NOT NULL AND R.voters IS NOT NULL;

CREATE TABLE final.serie(
serie_id SERIAL UNIQUE NOT NULL,
title text,
serie_started integer,
quarter text,
episode_name text,
episode_date text,
season_nr integer,
episode_nr integer,
episode_year integer,
state text,
rating_major integer,
rating_minor integer,
voters integer
);

INSERT INTO final.serie(
title, serie_started, quarter, episode_name, episode_date,
season_nr, episode_nr, episode_year, state)
SELECT title, serie_started, quarter, episode_name, episode_date,
season_nr, episode_nr, episode_year, state
FROM staging.series;
-- einde Denny

-- start Romeo
CREATE TABLE final.serie_location (
	serie_id int not null,
	location_id int not null,
  PRIMARY KEY (serie_id,location_id),
  CONSTRAINT fk_serie_location_serie_id
		FOREIGN KEY (serie_id)
		REFERENCES serie (serie_id),
	CONSTRAINT fk_movie_location_location_id
		FOREIGN KEY (location_id)
		REFERENCES location (location_id)
);

INSERT INTO serie_location (serie_id, location_id)
SELECT S.serie_id, L.location_id FROM final.serie S
INNER JOIN staging.location_series SL ON (S.title=SL.title)
	AND S.serie_started=SL.year_of_release OR (S.serie_started= NULL AND SL.year_of_release=NULL)
	AND S.quarter=SL.quarter OR (S.quarter=NULL AND SL.quarter=NULL)
	AND S.episode_name=SL.episode_name OR (S.episode_name=NULL AND SL.episode_name=NULL)
	AND S.season_nr=SL.season_nr OR (S.season_nr=NULL AND SL.season_nr=NULL)
	AND S.episode_nr=SL.episode_nr OR (S.episode_nr=NULL AND SL.episode_nr=NULL)
INNER JOIN location L ON SL.location=L.location
ON CONFLICT DO NOTHING;
-- einde Romeo

-- start Jacob
CREATE TABLE final.serie_genre (
	serie_id SERIAl NOT NULL,
	genre_id SERIAL NOT NULL,
	CONSTRAINT "fk_movie_genre_serie_id"
 		FOREIGN KEY (serie_id)
		REFERENCES final.serie(serie_id),
 	CONSTRAINT "fk_movie_genre_genre_id"
 		FOREIGN KEY (genre_id)
		REFERENCES final.genre(genre_id),
	PRIMARY KEY (serie_id,genre_id)
);

INSERT INTO final.serie_genre (serie_id, genre_id)
SELECT S.serie_id, G.genre_id FROM final.serie S
INNER JOIN staging.genres SG ON S.title=SG.serie
	AND S.serie_started=SG.year_of_release OR (S.serie_started= NULL AND SG.year_of_release=NULL)
	AND S.quarter=SG.quarter OR (S.quarter=NULL AND SG.quarter=NULL)
	AND S.episode_name=SG.episode_name OR (S.episode_name=NULL AND SG.episode_name=NULL)
	AND S.season_nr=SG.season_nr OR (S.season_nr=NULL AND SG.season_nr=NULL)
	AND S.episode_nr=SG.episode_nr OR (S.episode_nr=NULL AND SG.episode_nr=NULL)
INNER JOIN final.genre G ON SG.genre=G.genre
ON CONFLICT DO NOTHING;
-- einde Jacob

-- start Denny

UPDATE final.serie S
SET rating_major = R.rating_major,
rating_minor = R.rating_minor,
voters = R.voters
FROM staging.rating_series R
WHERE CONCAT('"', CONCAT(S.title, '"')) = R.title AND
(S.episode_name = R.episode_name OR
 (S.episode_name IS NULL AND R.episode_name IS NULL))
	  AND (S.season_nr = R.season_nr OR
      (S.season_nr IS NULL AND R.season_nr IS NULL))
	  AND (S.episode_nr = R.episode_nr OR
      (S.episode_nr IS NULL AND R.episode_nr IS NULL))
AND R.rating_major IS NOT NULL AND R.rating_minor IS NOT NULL AND R.voters IS NOT NULL;
-- einde Denny
-- start Hindrik


CREATE INDEX actors_id_idx ON final.actors(actor_id);
CREATE INDEX actors_name_idx ON final.actors(actor_name);

CREATE INDEX movie_id_idx ON final.movie(movieid);
CREATE INDEX movie_title_idx ON final.movie(title);

CREATE INDEX serie_id_idx ON final.serie(serieid);
CREATE INDEX serie_title_idx ON final.serie(title);

CREATE TABLE final.movie_actors
(
  movie_id INTEGER NOT NULL,
  actor_id INTEGER NOT NULL,
  voice TEXT,
  credited_as TEXT,
  character_name TEXT,
  billing_position INTEGER,
  CONSTRAINT fk_movie_actors_movie_id
    FOREIGN KEY (movie_id)
    REFERENCES final.movie(movieid),
  CONSTRAINT  fk_movie_actors_actor_id
    FOREIGN KEY (actor_id)
    REFERENCES final.actors(actor_id),
  CONSTRAINT  pk_mvoie_actors
    PRIMARY KEY (movie_id,actor_id)
);

CREATE TABLE final.serie_actors
(
  serie_id INTEGER NOT NULL,
  actor_id INTEGER NOT NULL,
  voice TEXT,
  credited_as TEXT,
  character_name TEXT,
  billing_position INTEGER,
  CONSTRAINT fk_serie_actors_serie_id
    FOREIGN KEY (serie_id)
    REFERENCES final.serie(serieid),
  CONSTRAINT  fk_serie_actors_actor_id
    FOREIGN KEY (actor_id)
    REFERENCES final.actors(actor_id),
  CONSTRAINT  pk_serie_actors
    PRIMARY KEY (serie_id,actor_id)
);