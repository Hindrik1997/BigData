package VZ.Commands;

import VZ.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hindrik on 25-1-17.
 */


/**
 * Base class which governs the usage of commands. Provides a way to show a 'processing' message and shows a warning if
 * anything goes wrong
 */
public abstract class Command extends AbstractAction implements ActionListener {

    /**
     * Catches the event and forwards to the action() function
     * @param actionEvent action event
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        JFrame frame = new JFrame("Verwerken...");
        JPanel panel = new JPanel(new GridLayout(0,1));
        JLabel label = new JLabel("Bezig met verwerken...", SwingConstants.CENTER);
        panel.add(label);
        frame.add(panel);
        panel.setMinimumSize(new Dimension(200,100));
        panel.setPreferredSize(new Dimension(200,100));
        panel.setMaximumSize(new Dimension(200,100));
        frame.setUndecorated(true);

        Main.getInstance().getMainFrame().setEnabled(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
        Point newLocation = new Point(middle.x - (frame.getWidth() / 2),
                middle.y - (frame.getHeight() / 2));
        frame.setLocation(newLocation);

        frame.setAlwaysOnTop(true);

        frame.pack();
        frame.setVisible(true);
        frame.repaint();
        SwingUtilities.invokeLater(() -> {
            try
            {
                action(actionEvent);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                frame.setVisible(false);
                JOptionPane.showMessageDialog(Main.getInstance().getMainFrame(), "Er is een fout opgetreden tijdens het verwerken van de vraag.", "Fout", JOptionPane.ERROR_MESSAGE);
            }
            finally
            {
                frame.setVisible(false);
                frame.dispose();
                Main.getInstance().getMainFrame().setEnabled(true);
            }
        });
    }

    /**
     * Abstract function defining the command that must be executed. Must be overridden in derived classes!
     * @param actionEvent action event
     */
    public abstract void action(ActionEvent actionEvent);
}