package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.RQuestionBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hindrik on 28-1-17.
 */
public class VerbandSeasonRatings extends Command {

    @Override
    public void action(ActionEvent actionEvent) {

        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                new RQuestionBase("<html>Onderzoek of er een verband is tussen het aantal seizoenen dat van series wordt gemaakt en de gemiddelde ratings van de series.<br><br>",
                        "<html>CREATE VIEW avg_rating_season_number AS SELECT season_number, avg(rating_serie) AS average_rating FROM (SELECT max(season_nr) AS season_number, avg(rating_season) AS rating_serie FROM (SELECT title, season_nr, avg(rating_major + 0.1*rating_minor) AS rating_season FROM serie S WHERE season_nr IS NOT NULL AND rating_major IS NOT NULL AND rating_minor IS NOT NULL GROUP BY title, season_nr) AS season_rating GROUP BY title) AS serie_rating WHERE season_number < 100 GROUP BY season_number<br><br>",
                        new ArrayList<>(Arrays.asList("> avgRatingSeasonNumber <- dbReadTable(con, c(\"final\",\"avg_rating_season_number\"))","> model=lm(average_rating~., data= avgRatingSeasonNumber)", "> plot(avgRatingSeasonNumber)")),
                        "console2.jpg",
                        "result2.jpg",
                        "<html><br>CONSLUSIE: Uit bovenstaande onderzoek, blijkt dat er nauwelijks tot geen verband bestaat tussen het aantal seizoenen dat van een serie wordt gemaakt en de gemiddelde ratings van de bijbehorende series. We zien dat er tot ongeveer 15 seizoenen het gemiddelde allemaal rond de 7.5 ligt. Daarna is er geen duidelijke lijn door te trekken. Ook zien we een hele lage significatie en daaruit maken wij op dat er geen verband is tussen het aantal seizoenen en de rating van een serie."
                )
        ));
    }
}
