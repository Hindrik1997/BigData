package VZ.Commands;

import VZ.Main;

import java.awt.event.ActionEvent;

/**
 * Created by hindrik on 25-1-17.
 */
public class MainMenuCommand extends Command {

    @Override
    public void action(ActionEvent actionEvent) {
        Main.getInstance().setMainJPanel(null);
    }
}
