-- Database: BigData

-- DROP DATABASE "BigData";

CREATE DATABASE "BigData"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Dutch_Netherlands.1252'
    LC_CTYPE = 'Dutch_Netherlands.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	

-- Table: public.actors

-- DROP TABLE public.actors;

CREATE TABLE public.actors
(
  id SERIAL NOT NULL,
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
  gender text,
  CONSTRAINT "ID_PK_A" PRIMARY KEY (id)
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
    id integer NOT NULL,
    title text COLLATE pg_catalog."default" NOT NULL,
	year integer,
    quarter text COLLATE pg_catalog."default",
    platform text COLLATE pg_catalog."default",
    state text COLLATE pg_catalog."default",
    CONSTRAINT movies_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.movies
    OWNER to postgres;
	
	
-- Table: public.series

-- DROP TABLE public.series;

CREATE TABLE public.series
(
    id integer NOT NULL,
    title text COLLATE pg_catalog."default" NOT NULL,
    "serieStarted" integer,
    quarter text COLLATE pg_catalog."default",
    "episodeName" text COLLATE pg_catalog."default",
    "episodeDate" text COLLATE pg_catalog."default",
    "seasonNr" integer,
    "episodeNr" integer,
    "episodeYear" integer,
    state text COLLATE pg_catalog."default",
    CONSTRAINT series_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.series
    OWNER to postgres;
	
-- Table: public.biografies

-- DROP TABLE public.biografies;

CREATE TABLE public.biografies
(
    id integer NOT NULL,
    name text COLLATE pg_catalog."default",
    "realName" text COLLATE pg_catalog."default",
    height text COLLATE pg_catalog."default",
    birthdate text COLLATE pg_catalog."default",
    "birthLocation" text COLLATE pg_catalog."default",
    deathdate text COLLATE pg_catalog."default",
    "deathLocation" text COLLATE pg_catalog."default",
    "causeOfDeath" text COLLATE pg_catalog."default",
    CONSTRAINT biografies_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.biografies
    OWNER to postgres;
	
-- Table: public.genres

-- DROP TABLE public.genres;

CREATE TABLE public.genres
(
    id integer NOT NULL,
    movie text COLLATE pg_catalog."default",
    serie text COLLATE pg_catalog."default",
    year text COLLATE pg_catalog."default",
    quarter text COLLATE pg_catalog."default",
    state text COLLATE pg_catalog."default",
    "epsName" text COLLATE pg_catalog."default",
    "seasonNr" integer,
    "episodeNr" integer,
    platform text COLLATE pg_catalog."default",
    genre text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT genres_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.genres
    OWNER to postgres;

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
'C:/Users/denny/Downloads/BigData/Starter/actors.csv'
DELIMITER AS '|' NULL '' ENCODING 'ISO_8859_5';

COPY public.biografies(
  name, realName, height, birthdate, birthLocation, deathdate, deathLocation, causeOfDeath)
FROM
'C:/Users/denny/Downloads/BigData/Starter/biography.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.genres(
  movie, serie, year, quarter, quarter, state, epsName, seasonNr, episodeNr, platform, genre)
FROM
'C:/Users/denny/Downloads/BigData/Starter/genre.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.movies(
  title, year, quarter, platform, state)
FROM
'C:/Users/denny/Downloads/BigData/Starter/movies.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';

COPY public.series(
  title, serieStarted, quarter, episodeName, episodeDate, seasonNr, episodeNr, episodeYear, state)
FROM
'C:/Users/denny/Downloads/BigData/Starter/series.csv'
DELIMITER AS '|' NULL 'null' ENCODING 'ISO_8859_5';