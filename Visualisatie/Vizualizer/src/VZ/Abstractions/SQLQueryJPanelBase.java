package VZ.Abstractions;

import VZ.Main;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by hindrik on 25-1-17.
 */
public class SQLQueryJPanelBase extends JPanel {

    public SQLQueryJPanelBase(String question, String query)
    {
        setLayout(new BorderLayout());

        ResultSet rs = Main.getInstance().executeQuery(query);
        ResultSetMetaData md = null;
        try {
             md = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert md != null;
        int columns = 0;
        try {
            columns = md.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Vector<String> columnNames = new Vector<>();
        Vector<Object> data = new Vector<>();

        for (int i = 1; i <= columns; i++)
        {
            try {
                columnNames.addElement( md.getColumnName(i) );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            while (rs.next())
            {
                Vector<Object> row = new Vector<>(columns);

                for (int i = 1; i <= columns; i++)
                {
                    row.addElement( rs.getObject(i) );
                }

                data.addElement( row );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames)
        {
            @Override
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        JTable table = new JTable( model );
        JScrollPane scrollPane = new JScrollPane( table );
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel top_panel = new JPanel(new GridLayout(0,1));
        top_panel.add(new JLabel("Vraag: " + question));
        top_panel.add(new JLabel("<html><br>"));
        top_panel.add(new JLabel("Query: " + query));
        top_panel.add(new JLabel("<html><br>"));
        top_panel.add(new JLabel("Resultaat: "));
        add(top_panel, BorderLayout.PAGE_START);
    }
}