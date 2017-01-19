CREATE SCHEMA final;
CREATE TABLE final.movie(
movieId SERIAL UNIQUE NOT NULL,
title text,
year_of_release integer,
quarter text,
platform text,
state text,
rating_major integer,
rating_minor integer
);

CREATE INDEX title_movie_idx ON final.movie(title);

INSERT INTO final.movie(
title, year_of_release, quarter, platform, state)
SELECT title, year_of_release, quarter, platform, state
FROM staging.movies;

UPDATE final.movie M
SET rating_major = R.rating_major,
rating_minor = R.rating_minor
FROM staging.rating_movies R
WHERE M.title = R.title AND
(M.year_of_release = R.year_of_release OR
 (M.year_of_release IS NULL AND R.year_of_release IS NULL))
 AND (M.quarter = R.quarter OR
      (M.quarter IS NULL AND R.quarter IS NULL))
AND R.rating_major IS NOT NULL AND R.rating_minor IS NOT NULL;