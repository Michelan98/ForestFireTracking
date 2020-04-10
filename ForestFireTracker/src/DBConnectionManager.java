import java.sql.*;

class QueryResult {
    public ResultSet resultSet;
    public ResultSetMetaData metaData;
    public QueryResult(ResultSet resultSet, ResultSetMetaData metaData) {
        this.resultSet = resultSet;
        this.metaData = metaData;
    }
}

public class DBConnectionManager {
    private static Connection connection = null;
    private static Statement stmt = null;
    // Method that returns a connection object to the database
    public static boolean connect() {

        if (connection != null) {
            try {
                if (connection.isValid(100)) {
                    return true;
                }
            } catch (SQLException ignored) {
                connection = null;
            }
        }

        // Parameters for connecting to the database
        final String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        final String password = "FFT1234!";
        final String username = "cs421g12";

        // Need to register JDBC postgresql driver
        try {
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        } catch (Exception cnfe){
            System.err.println(cnfe.getMessage());
            return false;
        }

        try{
            // Try connecting to the database with url,username and password
            connection = DriverManager.getConnection(url, username,password);

        }catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean disconnect() {
        if (connection == null) return false;
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error while closing the connection");
        }
        connection = null;
        return true;
    }

    public static void beginStatement() {
        try {
            assert connection != null && connection.isValid(100);
            stmt = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void endStatement() {
        try {
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Statement getStatement() {
        return stmt;
    }

    public static PreparedStatement prepareStatement(String query) {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int executeUpdate(PreparedStatement stmt) {
        var res = 0;
        try {
            res = stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
        return res;
    }

    public static QueryResult executeQuery(PreparedStatement stmt) {
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        try {
            resultSet = stmt.executeQuery();
            metaData = resultSet.getMetaData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return new QueryResult(resultSet, metaData);

    }

    public static QueryResult executeStatement(String sql) {
        assert stmt != null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        try {
            stmt.execute(sql);
            resultSet = stmt.getResultSet();
            metaData = resultSet.getMetaData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return new QueryResult(resultSet, metaData);
    }
}
