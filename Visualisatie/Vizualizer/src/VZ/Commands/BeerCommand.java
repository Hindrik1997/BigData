package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 27-1-17.
 */
public class BeerCommand extends Command {
    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new SQLQueryJPanelBase(
                                "In welk jaar tussen 1990 en nu zijn de meeste films met het woord ‘beer’ in de titel geproduceerd. En wat is het meest voorkomende genre?",
                                "SELECT * FROM " +
                                        "(SELECT year_of_release, COUNT(year_of_release) " +
                                        "FROM final.films_beer_view " +
                                        "GROUP BY year_of_release " +
                                        "ORDER BY COUNT(year_of_release) DESC " +
                                        "LIMIT 1) AS A, " +
                                        "(SELECT genre, COUNT(genre) " +
                                        "FROM final.films_beer_view " +
                                        "GROUP BY genre " +
                                        "ORDER BY COUNT(genre) DESC " +
                                        "LIMIT 1) AS B"
                        )
                )
        );
    }
}
