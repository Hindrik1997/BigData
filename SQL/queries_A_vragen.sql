--A3
--levert de naam van de acteur met de langste filmcarrière
SELECT  actor_name, MAX(year_of_release) - MIN(year_of_release) AS career_length FROM
  final.actors_years_view GROUP BY actor_name ORDER BY career_length DESC LIMIT 1;

--A9
--levert de namen van alle films waarvan new york voorkomt in één of meer van de locaties
SELECT DISTINCT title from final.movie M INNER JOIN final.movie_location ML ON ML.movie_id = M.movie_id
  INNER JOIN final.location L ON ML.location_id = L.location_id AND L.location LIKE '%New York%';

--A15
--levert de naam van de acteur/actrioe die het vaakst heeft gespeeld in films die lager dan een 5 zijn beoordeeld
SELECT tmp.actor_name, COUNT(tmp.actor_name) AS roles FROM (SELECT A.actor_name FROM final.actors A INNER JOIN final.movie_actors MA ON MA.actor_id = A.actor_id
INNER JOIN final.movie M ON MA.movie_id = M.movie_id AND M.rating_major < 5) AS tmp GROUP BY actor_name ORDER BY roles DESC LIMIT 1;

--A16
--Zoekt welk jaar en welk genre het vaakst voorkomen in films met het woord 'Beer' in de titel

SELECT * FROM
(SELECT year_of_release, COUNT(year_of_release)
FROM final.films_beer_view
GROUP BY year_of_release
ORDER BY COUNT(year_of_release) DESC
LIMIT 1) AS A,
(SELECT genre, COUNT(genre)
FROM final.films_beer_view
GROUP BY genre
ORDER BY COUNT(genre) DESC
LIMIT 1) AS B;

--Eigen vraag 1
--levert de 10 meest voorkomende waardes van death_location in de acteurs tabel
SELECT death_location, COUNT(death_location)
FROM final.actors
GROUP BY death_location
ORDER BY COUNT(death_location) DESC
LIMIT 10;

--Eigen vraag 2
--levert het best beoordelde genre van 2016 (of een ander jaar, kan evt. variabel gemaakt worden)
SELECT DISTINCT genre, AVG(rating_major + (rating_minor/10)) AS avg_rating FROM final.genre_ratings_view GROUP BY genre ORDER BY avg_rating DESC LIMIT 1;