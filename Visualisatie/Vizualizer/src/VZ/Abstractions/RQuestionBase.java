package VZ.Abstractions;

import sun.security.rsa.RSACore;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hindrik on 28-1-17.
 */
public class RQuestionBase extends JPanel {

    public RQuestionBase(String question, String SQL, List<String> RScript, String image0, String image1, String conclusie)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel(question, SwingConstants.LEFT));
        add(new JLabel(SQL, SwingConstants.LEFT));
        for(String s : RScript)
        {
            add(new JLabel(s, SwingConstants.LEFT));
        }
        JPanel t = new JPanel(new GridLayout(0,2));
        t.add(new ImagePanelBase(image0, 500,300));
        t.add(new ImagePanelBase(image1,500,300));
        t.setPreferredSize(new Dimension(1200,300));
        t.setMinimumSize(new Dimension(1200,300));
        t.setMaximumSize(new Dimension(1200,300));
        add(t);
        add(new JLabel(conclusie, SwingConstants.LEFT));

    }









}
