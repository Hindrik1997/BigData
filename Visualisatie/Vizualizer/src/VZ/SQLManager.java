package VZ;

import java.sql.*;

/**
 * Created by hindrik on 25-1-17.
 */


/**
 * Class which abstracts the connection to the database
 */
class SQLManager {

    private Connection connection = null;

    /**
     * Default constructor which sets up the postgres driver
     */
    SQLManager()
    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BigData", "postgres", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("Connectie met de database is geslaagd!");
        }
    }

    /**
     * Allows for the execution of a query on the database
     * @param query query to execute
     * @return result of the query
     */
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


    /**
     * Closes the connection to the database
     */
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

    /**
     * Special function for the GC, to close the connection. Only provided for completeness
     * @throws Throwable exceptions!
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}