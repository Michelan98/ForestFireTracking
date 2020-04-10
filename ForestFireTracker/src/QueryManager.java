import java.sql.SQLException;
import java.util.ArrayList;

public class QueryManager {
    /*
        TODO: write all queries in here (processing can be done elsewhere but let's keep the queries in one place

        for each method:
            - Connect to DB: DBConnectionManager.connect()
            - Do your stuff, the DBConnectionManager handles the connection object, check it's methods
            - Disconnect from the DB: DBConnectionManager.disconnect()
     */

    public static void thing() {
        // Kind of an example
        DBConnectionManager.connect();
        try{
            var forestName = "Amazon Rain forest";
            var sqlStatement = ("select * from zones where fname = ? ");

            var preparedStatement = DBConnectionManager.prepareStatement(sqlStatement);
            assert preparedStatement != null;
            preparedStatement.setString(1, forestName);

            var result = DBConnectionManager.executeQuery(preparedStatement);
            assert result != null;
            var resultSet = result.resultSet;
            var metaData = result.metaData;

            int columnsNumber = metaData.getColumnCount();

            while(resultSet.next()){
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(metaData.getColumnName(i) + ": " + columnValue);
                }
                System.out.println("");
            }
        } catch(SQLException se) {
            int count = 1;
            while (se != null) {
                System.out.println("SQLException " + count);
                System.out.println("Code: " + se.getErrorCode());
                System.out.println("SqlState: " + se.getSQLState());
                System.out.println("Error Message: " + se.getMessage());
                se = se.getNextException();
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DBConnectionManager.disconnect();
    }

    /**
     * Returns the list of forest names from the DB
     * @return ArrayList of strings containing the names
     */
    public static ArrayList<String> getForestNamesFromDB() {
        var ret = new ArrayList<String>();
        var sql = "Select fname From Forests";
        if (!DBConnectionManager.connect()) return null;
        DBConnectionManager.beginStatement();
        try {
            var res = DBConnectionManager.executeStatement(sql);
            if (res != null) {
                assert res.metaData.getColumnCount() == 1;
                while (res.resultSet.next()) {
                    ret.add(res.resultSet.getString(1));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            DBConnectionManager.endStatement();
            DBConnectionManager.disconnect();
            return null;
        }
        DBConnectionManager.endStatement();
        DBConnectionManager.disconnect();
        return ret;
    }
}
