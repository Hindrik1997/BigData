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
public class Main {

    private static Main _instance;
    public static Main getInstance()
    {
        return _instance;
    }

    private MainView _main_view = null;
    private SQLManager _main_model = null;


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


    public static void main(String[] args)
    {
        _instance = new Main();
    }

    public void closeDatabaseConnection()
    {
        _main_model.close();
    }

    public void setMainJPanel(JPanel panel)
    {
        _main_view.setMainJPanel(panel);
    }

    public ResultSet executeQuery(String query)
    {
        return _main_model.executeQuery(query);
    }

    public JFrame getMainFrame()
    {
        return _main_view;
    }









}
