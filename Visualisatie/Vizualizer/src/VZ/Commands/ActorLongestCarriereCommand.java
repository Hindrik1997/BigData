package VZ.Commands;

import VZ.Main;
import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 25-1-17.
 */
public class ActorLongestCarriereCommand extends Command {

    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new SQLQueryJPanelBase(
                                "Welke acteur (m/v) heeft de langste filmcarrière? (dus geen series)",
                                "SELECT actor_name, MAX(year_of_release) - MIN(year_of_release) AS career_length FROM final.actors_years_view GROUP BY actor_name ORDER BY career_length DESC LIMIT 100"
                        )
                )
        );
    }
}