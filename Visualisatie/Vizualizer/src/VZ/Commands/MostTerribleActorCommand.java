package VZ.Commands;

import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 26-1-17.
 */
public class MostTerribleActorCommand extends Command {
    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new SQLQueryJPanelBase(
                                "Welke acteur of actrice speelt het meest in de slechtst gewaardeerde<br> films?",
                                "SELECT tmp.actor_name, COUNT(tmp.actor_name) AS roles FROM (SELECT A.actor_name FROM final.actors A INNER JOIN final.movie_actors MA ON MA.actor_id = A.actor_id\n" +
                                        "INNER JOIN final.movie M ON MA.movie_id = M.movie_id AND M.rating_major < 5) AS tmp GROUP BY actor_name ORDER BY roles DESC LIMIT 10"
                        )
                )
        );
    }
}
