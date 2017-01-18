ALTER TABLE staging.movies ADD COLUMN rating_major integer;
ALTER TABLE staging.movies ADD COLUMN rating_minor integer;
ALTER TABLE staging.movies ADD COLUMN voters integer;

UPDATE staging.movies SET 
rating_major = b.rating_major,
rating_minor = b.rating_minor, 
voters = b.voters
FROM staging.movies a INNER JOIN staging.rating_movies b
ON a.title = b.title

ALTER TABLE staging.series ADD COLUMN rating_major integer;
ALTER TABLE staging.series ADD COLUMN rating_minor integer;
ALTER TABLE staging.series ADD COLUMN voters integer;

UPDATE staging.movies SET 
rating_major = b.rating_major,
rating_minor = b.rating_minor, 
voters = b.voters
FROM staging.movies a INNER JOIN staging.rating_movies b
ON a.title = b.title AND (a.episode_name = b.episode_name OR 
a.episode_date = b.episode_date OR 
(a.season_nr = b.season_nr AND a.episode_nr = b.episode_nr))