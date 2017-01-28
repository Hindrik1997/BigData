package VZ.Abstractions;

import VZ.Commands.Command;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by hindrik on 25-1-17.
 */
public class ButtonAndLabel extends JPanel {

    private JPanel _inner_panel = null;
    private JLabel _label = null;
    private JButton _button = null;

    public ButtonAndLabel(String button_text, String label_text, Dimension size)
    {
        super(new GridLayout(1,1));
        _label = new JLabel(label_text);
        _button = new JButton(button_text);
        _inner_panel = new JPanel(new GridLayout(2,1));
        this.setPreferredSize(size);
        Border padding = BorderFactory.createEmptyBorder(10,10,10,10);
        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        _inner_panel.setBorder(padding);
        this.setBorder(etched);
        _inner_panel.add(_label);
        _inner_panel.add(_button);
        this.add(_inner_panel);
    }

    public void removeActionListeners()
    {
        for(ActionListener al : _button.getActionListeners())
        {
            _button.removeActionListener(al);
        }
    }

    public void setActionListener(Command command)
    {
        _button.addActionListener(command);
    }
}
