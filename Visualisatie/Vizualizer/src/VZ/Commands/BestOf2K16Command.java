package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 27-1-17.
 */
public class BestOf2K16Command extends Command {
    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new SQLQueryJPanelBase(
                                "Welk filmgenre werd gemiddeld het best <br>beoordeeld in het jaar 2016?",
                                "SELECT DISTINCT genre, AVG(rating_major + (rating_minor/10)) AS avg_rating FROM final.genre_ratings_view GROUP BY genre ORDER BY avg_rating DESC LIMIT 1"
                        )
                )
        );
    }
}
