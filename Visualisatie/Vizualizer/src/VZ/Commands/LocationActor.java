package VZ.Commands;

import VZ.Abstractions.MapsPanel;
import VZ.Abstractions.QuestionJPanelBase;
import VZ.Abstractions.SQLQueryJPanelBase;
import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 28-1-17.
 */
public class LocationActor extends Command {

    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(
                new QuestionJPanelBase(
                        new MapsPanel(false)
                )
        );
    }
}
