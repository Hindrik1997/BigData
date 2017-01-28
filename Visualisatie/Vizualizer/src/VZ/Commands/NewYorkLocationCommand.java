package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 26-1-17.
 */
public class NewYorkLocationCommand extends Command {
    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new SQLQueryJPanelBase(
                                "Welke films spelen (gedeeltelijk) in New York?",
                                "SELECT DISTINCT title from final.movie M INNER JOIN final.movie_location ML ON ML.movie_id = M.movie_id\n" +
                                        "  INNER JOIN final.location L ON ML.location_id = L.location_id AND L.location LIKE '%New York%'"
                        )
                )
        );
    }
}
