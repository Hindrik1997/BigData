package VZ;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by hindrik on 25-1-17.
 */


/***
 * Main class of the application. Acts as a controller.
 * Is also a singleton. Handles the main() function of the application and ties together a lot of stuff.
 */
public class Main {

    private static Main _instance;
    public static Main getInstance()
    {
        return _instance;
    }

    private MainView _main_view = null;
    private SQLManager _main_model = null;


    /**
     * Main class constructor. Sets the application wide settings and creates a model (SQLManager) and a view (MainView)
     */
    private Main()
    {


        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try
            {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException f)
            {
                f.printStackTrace();
            }
        }


        _main_view = new MainView();
        _main_model = new SQLManager();

    }

    /**
     * Application entry point
     * @param args are not used
     */
    public static void main(String[] args)
    {
        _instance = new Main();
    }

    /**
     * Handles the closing of the database connection.
     */
    public void closeDatabaseConnection()
    {
        _main_model.close();
    }

    /**
     * Allows to set the main panel of the view. In this way you can easily set the interior parts of the application and the view of the data,
     * but keep the control of the JFrame tied to the view class.
     * @param panel
     */
    public void setMainJPanel(JPanel panel)
    {
        _main_view.setMainJPanel(panel);
    }

    /**
     * Executes a query on the backend tied to the application in a safe abstracted way.
     * @param query the query to execute
     * @return a ResultSet representing the results of the query.
     */
    public ResultSet executeQuery(String query)
    {
        return _main_model.executeQuery(query);
    }

    /**
     * Returns a reference to the JFrame of the application.
     * @return reference to the JFrame
     */
    public JFrame getMainFrame()
    {
        return _main_view;
    }

}
