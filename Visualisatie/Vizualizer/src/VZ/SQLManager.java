package VZ;

import java.sql.*;

/**
 * Created by hindrik on 25-1-17.
 */
class SQLManager {

    private Connection connection = null;

    SQLManager()
    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bigdata", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("Connectie met de database is geslaagd!");
        }
    }

    ResultSet executeQuery(String query)
    {
        Statement s = null;
        try {
            s = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = null;
        try {
            assert s != null;
            rs = s.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert rs != null;
        return rs;
    }



    void close()
    {
        System.out.println("De connectie met de database wordt gesloten...");
        try {
            if(!connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}