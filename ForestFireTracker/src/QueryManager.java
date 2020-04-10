import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public static void listForests() {
        DBConnectionManager.connect();
        var con = DBConnectionManager.getConnection();
        var stmt = DBConnectionManager.getStatement();
        try{
            DBConnectionManager.beginStatement();
            String query = "SELECT fname FROM forests";
            PreparedStatement pst = DBConnectionManager.prepareStatement(query);
            assert pst != null;
            DBConnectionManager.endStatement();
            DBConnectionManager.executeUpdate(pst);
            QueryResult result = DBConnectionManager.executeQuery(pst);
            assert result != null;
            var rs = result.resultSet;
            var md = result.metaData;
            int numColumns = md.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= numColumns; i++) {
                    if (md.getColumnName(i) == "fname") {
                        var resultColumn = rs.getArray(i);
                        System.out.print(md.getColumnName(i) + ": " + resultColumn.toString());
                    }
                }
                System.out.println("");
            }
        }catch(SQLException se) {
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
}
