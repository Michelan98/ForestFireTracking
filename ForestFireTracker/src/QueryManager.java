import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;

public class QueryManager {
    private static Entry<String, Float> forestAndSurface;
    /*
        TODO: write all queries in here (processing can be done elsewhere but let's keep the queries in one place

        for each method:
            - Connect to DB: DBConnectionManager.connect()
            - Do your stuff, the DBConnectionManager handles the connection object, check it's methods
            - Disconnect from the DB: DBConnectionManager.disconnect()
     */

    public static String format(ResultSet results, String col1, String col2, int column1, int column2){
        int i, width = 0;
        int row_count = 0;
        StringBuffer buffer = new StringBuffer();
        StringBuffer bar = new StringBuffer( );
        StringBuffer filler = new StringBuffer();
        try {
            int size1 = results.getMetaData().getColumnDisplaySize(column1);
            int size2 = results.getMetaData().getColumnDisplaySize(column2);
            width += size1 + size2;
            width += 3;
            for(i=0; i<width; i++) {
                bar.append('-');
            }
            bar.append('\n');
            buffer.append(bar.toString( ) + "|");
            String label1 = results.getMetaData().getColumnLabel(1);
            String label2 = results.getMetaData().getColumnLabel(2);
            int x;

            if( label1.length( ) > size1 ) {
                label1 = label1.substring(0, size1);
            }
            if( label2.length( ) > size2 ) {
                label2 = label2.substring(0, size2);
            }
            // If the label is shorter than the column,
            // pad it with spaces
            if( label1.length( ) < size1 ) {
                int j;
                x = (size1-label1.length( ))/2;
                for(j=0; j<x; j++) {
                    filler.append(' ');
                }
                label1 = filler + label1 + filler;
                if(label1.length( ) > size1 ) {
                    label1 = label1.substring(0, size1);
                }
                else {
                    while( label1.length( ) < size1 ) {
                        label1 += " ";
                    }
                }
            }
            // Add the column header to the buffer
            buffer.append(label1 + "|");

            if( label2.length( ) < size2 ) {
                int j;
                x = (size2-label2.length( ))/2;
                for(j=0; j<x; j++) {
                    filler.append(' ');
                }
                label2 = filler + label2 + filler;
                if(label2.length( ) > size2 ) {
                    label2 = label2.substring(0, size2);
                }
                else {
                    while( label2.length( ) < size2 ) {
                        label2 += " ";
                    }
                }
            }
            // Add the column header to the buffer
            buffer.append(label2 + "|");
            buffer.append("\n" + bar.toString( ));
            // Format each row in the result set and add it on
            while( results.next( ) ) {
                row_count++;
                buffer.append('|');
                filler.replace(0, filler.length() - 1, " ");
                // Format each column of the row
                if( col1.length( ) > size1 ) {
                    col1 = col1.substring(0, size1);
                }else{
                    int j;
                    x = (size1-col1.length( ))/2;
                    for(j=0; j<x; j++) {
                        filler.append(' ');
                    }
                    col1 = filler + col1 + filler;
                    if( col1.length( ) > size1 ) {
                        col1 = col1.substring(0, size1);
                    }
                    else {
                        while( col1.length( ) < size1 ) {
                            col1 += " ";
                        }
                    }
                }
                buffer.append(col1 + "|");

                if( col2.length( ) > size2 ) {
                    col2 = col2.substring(0, size2);
                }else{
                    int j;

                    x = (size2-col2.length( ))/2;
                    for(j=0; j<x; j++) {
                        filler.append(' ');
                    }
                    col2 = filler + col2 + filler;
                    if( col2.length( ) > size2 ) {
                        col2 = col2.substring(0, size2);
                    }
                    else {
                        while( col2.length( ) < size2) {
                            col2 += " ";
                        }
                    }
                }
                buffer.append(col2 + "|");
                buffer.append("\n");
            }
            // Stick a row count up at the top
            if( row_count == 0 ) {
                buffer = new StringBuffer("No rows selected.\n");
            }
            else if( row_count == 1 ) {
                buffer = new StringBuffer("1 row selected.\n" +
                        buffer.toString( ) +
                        bar.toString( ));
            }
            else {
                buffer = new StringBuffer(row_count + " rows selected.\n" +
                        buffer.toString( ) +
                        bar.toString( ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return buffer.toString();
    }
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

    public static ArrayList<String> getForestSurfaceAreas(){
        var queryResult = new ArrayList<String>();
        String query = "SELECT fname, fsurfaceArea FROM forests";
        if (!DBConnectionManager.connect()) return null;
        DBConnectionManager.beginStatement();
        try {
            var res = DBConnectionManager.executeStatement(query);
            if (res != null) {
                assert res.metaData.getColumnCount() == 2;
                while (res.resultSet.next()) {
                    var fNames = res.resultSet.getString(1);
                    var surfaceAreas = res.resultSet.getFloat(2);
                    var surfaceAreasString = Float.toString(surfaceAreas);
                    var entry = format(res.resultSet, fNames, surfaceAreasString, 1,2);
                    queryResult.add(entry);

                    /*String fNames = forestAndSurface.getKey();
                    fNames = res.resultSet.getString(1);
                    Float surfaceAreas = res.resultSet.getFloat(2);
                    forestAndSurface.setValue(surfaceAreas);

                    queryResult.add(0, forestAndSurface);*/
                    /*float surfaceAreas = res.resultSet.getFloat(2);
                    for (String fName : getForestNamesFromDB()){
                        queryResult.add(Integer.parseInt(fName), null);
                    }
                    queryResult.forEach(stringFloatEntry -> {
                            stringFloatEntry.setValue(surfaceAreas);
                    });*/
                }
            }
        }catch (Exception e) {
                System.err.println(e.getMessage());
                DBConnectionManager.endStatement();
                DBConnectionManager.disconnect();
                return null;
            }

        DBConnectionManager.endStatement();
        DBConnectionManager.disconnect();
        return queryResult;
    }
}
