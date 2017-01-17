-- Table: public.actors

-- DROP TABLE public.actors;

CREATE TABLE public.actors
(
  actor_name text,
  movie_title text,
  serie_title text,
  year_of_release integer,
  quarter text,
  state text,
  episode_date text,
  episode_name text,
  seasonNr integer,
  episodeNr integer,
  platform text,
  voice text,
  credited_as text,
  character_name text,
  billing_position integer,
  gender text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.actors
  OWNER TO postgres;

-- Table: public.movies

-- DROP TABLE public.movies;

CREATE TABLE public.movies
(
    title text  NOT NULL,
	  year integer,
    quarter text ,
    platform text ,
    state text
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE public.movies
    OWNER to postgres;


-- Table: public.series

-- DROP TABLE public.series;

CREATE TABLE public.series
(
    title text  NOT NULL,
    serie_started integer,
    quarter text ,
    episode_name text ,
    episode_date text ,
    season_nr integer,
    episode_nr integer,
    episode_year integer,
    state text
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE public.series
    OWNER to postgres;

-- Table: public.biografies

-- DROP TABLE public.biografies;

CREATE TABLE public.biografies
(
    name text ,
    real_name text ,
    height text ,
    birthdate text ,
    birth_location text ,
    deathdate text ,
    death_location text ,
    cause_of_death text
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE public.biografies
    OWNER to postgres;

-- Table: public.genres

-- DROP TABLE public.genres;

CREATE TABLE public.genres
(
    movie text ,
    serie text ,
    year text ,
    quarter text ,
    state text ,
    episode_name text ,
    season_nr integer,
    episode_nr integer,
    platform text ,
    genre text  NOT NULL
)
WITH (
    OIDS = FALSE
)
;

ALTER TABLE public.genres
    OWNER to postgres;




-- Table: public.location_series

-- DROP TABLE public.location_series;

CREATE TABLE public.location_series
(
  title text,
  year_of_release integer,
  quarter text,
  episode_name text,
  seasonNr integer,
  episodeNr integer,
  location text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.location_series
  OWNER TO postgres;

-- Table: public.location_movies
-- DROP TABLE public.location_movies;

CREATE TABLE public.location_movies
(
  title text,
  year_of_release integer,
  quarter text,
  medium text,
  state text,
  location text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.location_movies
  OWNER TO postgres;


COPY public.actors (
  actor_name,
  movie_title,
  serie_title,
  year_of_release,
  quarter,
  state,
  episode_date,
  episode_name,
  seasonnr, episodenr,
  platform,
  voice,
  credited_as,
  character_name,
  billing_position,
  gender
)
FROM
'/tmp/actors.csv'
DELIMITER AS '|' NULL '' ENCODING 'ISO_8859_5';

COPY public.biografies(
  name, real_name, height, birthdate, birth_location, deathdate, death_location, cause_of_death)
FROM
'/tmp/biography.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.movies(
  title, year, quarter, platform, state)
FROM
'/tmp/movies.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.series(
  title, serie_started, quarter, episode_name, episode_date, season_nr, episode_nr, episode_year, state)
FROM
'/tmp/series.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.location_series(
    title, year_of_release, quarter, episode_name, seasonnr, episodenr, location
)   FROM
'/tmp/locationseries.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.location_movies(
  title, year_of_release, quarter, medium, state, location)
FROM
'/tmp/locationmovies.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.genres(
  movie, serie, year, quarter, state, episode_name, season_nr, episode_nr, platform, genre)
FROM
'/tmp/genre.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';
