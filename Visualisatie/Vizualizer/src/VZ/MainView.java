package VZ;

import VZ.Abstractions.ButtonAndLabel;
import VZ.Commands.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by hindrik on 25-1-17.
 */

/**
 * Acts as the model of the application. Provides an abstracted way to visualize data.
 */
class MainView extends JFrame {
    private JPanel _main_panel = null;
    private JPanel _previous_panel = null;

    /**
     * Constructor. Handles the creation of the JFrame.
     * Defaults to showing a main menu.
     */
    MainView() {
        super("Hoofdmenu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _main_panel = new JPanel(new GridLayout(0, 2));
        _main_panel.setMinimumSize(new Dimension(1000, 1000));
        _main_panel.setPreferredSize(new Dimension(1000, 1000));
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        _main_panel.setBorder(border);
        setMainJPanel(_main_panel);
        initialize();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                Main.getInstance().closeDatabaseConnection();
                super.windowClosing(windowEvent);
            }
        });
    }

    /**
     * Initializes the buttons used for the questions in the main menu.
     * Loads in the questions using a file, for easy modification of the text.
     */
    private void initialize() {
        List<String> questionStrings = new ArrayList<>();
        List<Command> commands = setupCommands();

        try (Stream<String> stream = Files.lines(Paths.get("questions.txt"), StandardCharsets.UTF_8)) {
            stream.forEachOrdered((questionStrings::add));
        } catch (IOException io) {
            io.printStackTrace();
        }

        for (int i = 0; i < 10; ++i) {
            ButtonAndLabel bl = new ButtonAndLabel("Ga naar het antwoord", questionStrings.get(i), new Dimension(50, 50));
            bl.setActionListener(commands.get(i));
            _main_panel.add(bl);
        }
    }

    /**
     * Sets up the list of commands used for the questions
     * @return a list of command objects
     */
    private List<Command> setupCommands() {
        List<Command> commands = new ArrayList<>();

        commands.add(new ActorLongestCarriereCommand());
        commands.add(new NewYorkLocationCommand());
        commands.add(new MostTerribleActorCommand());
        commands.add(new BeerCommand());
        commands.add(new DeathliestLocationsCommand());
        commands.add(new BestOf2K16Command());

        commands.add(new LocationActor());

        commands.add(new LocationMovie());

        commands.add(new VerbandActricesAge());
        commands.add(new VerbandSeasonRatings());

        return commands;
    }

    /**
     * Sets the main JPanel of the application in a safe manner.
     * Defaults to the main menu.
     * @param panel panel to set. (Null -> main menu)
     */
    void setMainJPanel(JPanel panel) {
        this.getRootPane().getContentPane().removeAll();
        if (panel == null) {
            this.add(_main_panel);
        } else
            this.add(panel);
        forceRefresh();
    }

    /**
     * Forces refresing of the JFrame
     */
    void forceRefresh() {
        SwingUtilities.updateComponentTreeUI(this);
        this.invalidate();
        this.validate();
        this.repaint();
    }


}