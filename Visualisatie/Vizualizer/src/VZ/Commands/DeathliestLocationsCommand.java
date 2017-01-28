package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 26-1-17.
 */
public class DeathliestLocationsCommand extends Command {
    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new SQLQueryJPanelBase(
                                "Top 10 dodelijkste plek voor acteurs (meest voorkomende location of death)",
                                "SELECT death_location, COUNT(death_location) " +
                                        "FROM final.actors " +
                                        "GROUP BY death_location " +
                                        "ORDER BY COUNT(death_location) DESC " +
                                        "LIMIT 10"
                        )
                )
        );
    }
}
