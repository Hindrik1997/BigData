package VZ.Abstractions;

import VZ.Commands.MainMenuCommand;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by hindrik on 25-1-17.
 */

/**
 * Class that abstracts away the functionality for easy addition of question panels.
 * Allows for easy chaining of constructors, and is very similar to the decorator pattern.
 */
public class QuestionJPanelBase extends JPanel {

    public QuestionJPanelBase(JPanel content)
    {
        super();
        setLayout(new BorderLayout());
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton button = new JButton("Ga terug");
        button.addActionListener(new MainMenuCommand());
        topBar.add(button);
        add(topBar, BorderLayout.PAGE_START);
        JPanel inner = new JPanel(new GridLayout(0,1));
        inner.add(content);
        Border border = BorderFactory.createEmptyBorder(10,10,10,10);
        Border border2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        inner.setBorder(border);
        content.setBorder(border2);
        add(inner, BorderLayout.CENTER);
    }
}