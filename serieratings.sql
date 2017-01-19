CREATE TABLE final.serie(
serieId SERIAL UNIQUE NOT NULL,
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
rating_minor integer
);

CREATE INDEX title_serie_idx ON final.serie(title);

INSERT INTO final.serie(
title, serie_started, quarter, episode_name, episode_date,
season_nr, episode_nr, episode_year, state)
SELECT title, serie_started, quarter, episode_name, episode_date,
season_nr, episode_nr, episode_year, state
FROM staging.series;

UPDATE final.serie 
SET title = CONCAT('"',CONCAT(title,'"'));
  
UPDATE final.serie S
SET rating_major = R.rating_major,
rating_minor = R.rating_minor
FROM staging.rating_series R
WHERE S.title = R.title AND
(S.episode_name = R.episode_name OR
 (S.episode_name IS NULL AND R.episode_name IS NULL))
	  AND (S.season_nr = R.season_nr OR
      (S.season_nr IS NULL AND R.season_nr IS NULL))
	  AND (S.episode_nr = R.episode_nr OR
      (S.episode_nr IS NULL AND R.episode_nr IS NULL))
AND R.rating_major IS NOT NULL AND R.rating_minor IS NOT NULL;