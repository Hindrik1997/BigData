--A1


--A3

 
--A9
--levert de namen van alle films waarvan new york voorkomt in één of meer van de locaties
SELECT DISTINCT title from final.movie WHERE movie_id IN
(SELECT movie_id FROM final.movie_location WHERE location_id IN
(SELECT location_id FROM final.location WHERE location LIKE '%New York%')
);

--A15
--levert de naam van de acteur/actrice die het vaakst in films heeft gespeeld die lager dan een 5 zijn beoordeeld
SELECT actor_name, COUNT(actor_name) FROM final.actors WHERE actor_id IN
(SELECT actor_id FROM final.actor_movie WHERE movie_id IN 
(SELECT movie_id FROM final.movie WHERE rating_major < 5))
GROUP BY actor_name
ORDER BY COUNT(actor_name) DESC
LIMIT 1; 

--A16


--B3


--B8
--levert alle locaties voor een film met een bepaalde titel, deze kunnen gebruikt worden om een kaart te maken
SELECT location FROM final.location WHERE location_id IN 
(SELECT location_id FROM movie_location WHERE movie_id IN
(SELECT movie_id FROM final.movie WHERE title = '.....')
);

--C4


--C Eigen vraag


--Eigen vraag 1


--Eigen vraag 2
--levert de 10 meest voorkomende waardes van death_location in de acteurs tabel
SELECT death_location, COUNT(death_location)
FROM final.actors
GROUP BY death_location
ORDER BY COUNT(death_location) DESC
LIMIT 10;