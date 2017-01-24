--A1


--A3

 
--A9 WERKT
--levert de namen van alle films waarvan new york voorkomt in één of meer van de locaties
SELECT DISTINCT title from final.movie WHERE movie_id IN
(SELECT movie_id FROM final.movie_location WHERE location_id IN
(SELECT location_id FROM final.location WHERE location LIKE '%New York%')
);

--A15


--A16 WERKT
--maakt een view met alle filmtitels die het woord 'Beer' bevatten, met het jaar en genre erbij en zoekt welk jaar en welk genre het vaakst voorkomen
CREATE VIEW films_beer_view AS
(
SELECT M.title, M.year_of_release, G.genre FROM final.movie M, final.genre G WHERE G.genre_id IN 
(SELECT genre_id FROM final.movie_genre MG WHERE MG.movie_id = M.movie_id) AND M.year_of_release > 1990 AND M.title LIKE '% Beer %'
);
SELECT year_of_release, COUNT(year_of_release)
FROM films_beer_view
GROUP BY year_of_release
ORDER BY COUNT(year_of_release) DESC
LIMIT 1;

SELECT genre, COUNT(genre)
FROM films_beer_view
GROUP BY genre
ORDER BY COUNT(genre) DESC
LIMIT 1;

--B3


--B8 WERKT
--levert alle locaties voor een film met een bepaalde titel, deze kunnen gebruikt worden om een kaart te maken
SELECT location FROM final.location WHERE location_id IN 
(SELECT location_id FROM final.movie_location WHERE movie_id IN
(SELECT movie_id FROM final.movie WHERE title = '.....')
);

--C4


--C Eigen vraag


--Eigen vraag 1


--Eigen vraag 2 WERKT
--levert de 10 meest voorkomende waardes van death_location in de acteurs tabel
SELECT death_location, COUNT(death_location)
FROM final.actors
GROUP BY death_location
ORDER BY COUNT(death_location) DESC
LIMIT 10;