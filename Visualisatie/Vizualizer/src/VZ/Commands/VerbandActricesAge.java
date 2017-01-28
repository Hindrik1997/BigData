package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.RQuestionBase;
import VZ.Main;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hindrik on 28-1-17.
 */
public class VerbandActricesAge extends Command {

    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new RQuestionBase("<html>Er wordt vaak gezegd dat naarmate actrices ouder zijn zij moeilijker een rol krijgen. Ga (met R)<br> na of dat zo is. Je zou bijvoorbeeld een lineair regressiemodel kunnen maken met<br> leeftijd en geslacht als onafhankelijke variabelen en aantal films<br> per jaar als afhankelijke variabele.<br><br>",
                                "<html>CREATE VIEW age_count AS SELECT M.year_of_release - CAST(A.birth_date AS INTEGER)  AS age, COUNT(A.actor_id) AS<br> actresses_played_movies FROM final.actors A INNER JOIN final.movie_actors MA ON<br> A.actor_id=MA.actor_id INNER JOIN final.movie M ON MA.movie_id=M.movie_id<br> WHERE M.year_of_release IS NOT NULL AND A.birth_date IS NOT NULL AND A.gender = 'f' AND M.year_of_release - CAST(A.birth_date AS INTEGER)<br> BETWEEN 0 AND 100 GROUP BY M.year_of_release - CAST(A.birth_date AS INTEGER) ORDER BY age<br><br>",
                                new ArrayList<>(Arrays.asList("> ageCount <- dbReadTable(con, c(\"final\",\"age_count\"))","> model=lm(actresses_played_movies~., data=ageCount)", "> plot(ageCount)")),
                                "console.jpg",
                                "result.jpg",
                                "<html><br>CONSLUSIE: Uit de grafiek kun je afleiden dat de leeftijd van een actrices inderdaad invloed heeft op het aantal rollen.  De top zit rond de 24 jaar en naarmate zij ouder worden, krijgen zij steeds minder rollen. Het vermoeden wat in vraag C4 staat,<br> is dus waar. Ook zien we ook een hoge significatie en daaruit kun je opmaken, dat de leeftijd en het aantal rollen inderdaad invloed op elkaar heeft."
                        )
                ));
    }
}
