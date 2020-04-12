import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class QueryManager {
    /*
        TODO: write all queries in here (processing can be done elsewhere but let's keep the queries in one place

        for each method:
            - Connect to DB: DBConnectionManager.connect()
            - Do your stuff, the DBConnectionManager handles the connection object, check it's methods
            - Disconnect from the DB: DBConnectionManager.disconnect()
     */

    // Use to format functions by entering resultset, values for col 1 and 2 in String from resultSet.GetVar() fn
    // and the indices of the columns.
    // If you only want to format a SINGLE column, set "col2" to "" and "column2" to "0"
    public static String format(ResultSet results, String col1, String col2, int column1, int column2){
        StringBuffer buffer = new StringBuffer();
        try {
            int i, width = 0;
            int row_count = 0;
            String label2 = "";
            StringBuffer bar = new StringBuffer( );
            int size1 = results.getMetaData().getColumnDisplaySize(column1);
            int size2 = 0;
            if(col2 != "" && column2 > 0) {
                size2 = results.getMetaData().getColumnDisplaySize(column2);
                width += size1 + size2;
            }
            if(col2 == ""){
                width += 1;
            }else{
                width += 2;
            }

            for(i=0; i<width; i++) {
                bar.append('-');
            }
            bar.append('\n');
            buffer.append(bar.toString( ) + "|");
            String label1 = results.getMetaData().getColumnLabel(1);

            if( label1.length( ) > size1 ) {
                label1 = label1.substring(0, size1);
            }
            if(col2 != ""){
                label2 = results.getMetaData().getColumnLabel(2);
                if( label2.length( ) > size2 ) {
                    label2 = label2.substring(0, size2);
                }
            }

            // If the label is shorter than the column,
            // pad it with spaces
            if( label1.length( ) < size1 ) {
                StringBuffer filler = new StringBuffer();
                int j, x;
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

            if (col2 != ""){
                StringBuffer filler = new StringBuffer();
                if( label2.length( ) < size2 ) {
                    int j, x;
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
            }else{ }

            // Format each row in the result set and add it on
            while(results.next()) {
                row_count++;
                Object value1 = results.getObject(column1);
                Object value2 = results.getObject(column2);
                StringBuffer rowFiller1 = new StringBuffer();
                StringBuffer rowFiller2 = new StringBuffer();
                buffer.append('|');
                String str1;
                String str2;
                if( results.wasNull( ) ) {
                    str1 = "NULL";
                    str2 = "NULL";
                }
                else {
                    str1 = value1.toString( );
                    str2 = value2.toString();
                    str1 = str1.replaceAll("\n", "");
                    str2 = str2.replaceAll("\n", "");
                }
                // Format each column of the row
                if( str1.length() > size1 ) {
                    str1 = str1.substring(0, size1);
                }else{
                    int j, x;
                    x = (size1-str1.length( ))/2;
                    for(j=0; j<x; j++) {
                        rowFiller1.append(' ');
                    }
                    str1 = rowFiller1 + str1 + rowFiller1;
                    if( str1.length( ) > size1 ) {
                        str1 = str1.substring(0, size1);
                    }
                    else {
                        while( str1.length( ) < size1 ) {
                            str1 += " ";
                        }
                    }
                }
                buffer.append(str1 + "|");

                if (col2 != ""){
                    if( str2.length( ) > size2 ) {
                        str2 = str2.substring(0, size2);
                    }else{
                        int j, x;
                        x = (size2-str2.length( ))/2;
                        for(j=0; j<x; j++) {
                            rowFiller2.append(' ');
                        }
                        str2 = rowFiller2 + str2 + rowFiller2;
                        if( str2.length( ) > size2 ) {
                            str2 = str2.substring(0, size2);
                        }
                        else {
                            while( str2.length( ) < size2) {
                                str2 += " ";
                            }
                        }
                    }
                    str2.replace("\n", "");
                    buffer.append(str2 + "|");
                    buffer.append("\n");
                }else{ }

            }
            // Stick a row count up at the top
            if( row_count == 0) {
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
                    ret.add(res.resultSet.getString(1).trim());
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

    public static ArrayList<String> getForestSurfaceAreas() {
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
                    var entry = format(res.resultSet, fNames, surfaceAreasString, 1, 2);
                    queryResult.add(entry);
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
        return queryResult;
    }
    /**
     * Returns the list of species names and common names of animals from the DB
     * @return ArrayList of strings containing both species and common name
     */
    public static ArrayList<String> getSpeciesNamesFromDB() {
        //System.out.println("Querying...");
        var ret = new ArrayList<String>();
        var sql = "Select species From Animals";
        if (!DBConnectionManager.connect()) return null;
        DBConnectionManager.beginStatement();
        try {
            var res = DBConnectionManager.executeStatement(sql);
            if (res != null) {
                assert res.metaData.getColumnCount() == 2;
                while (res.resultSet.next()) {
                    var speciesName = res.resultSet.getString(1);
                    // FIX LATER
                    if (speciesName.equals("Endangered")) continue;
                    speciesName = String.format("%25s", speciesName);
                    var toAdd = speciesName.trim();
                    ret.add(toAdd);
                }
                //System.out.println("Finished!");
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

    /**
     * Returns the list of species names of endangered animals from the DB
     * @return ArrayList of strings containing both species name of endangered
     */
    public static ArrayList<String> getEndangeredSpeciesFromDB() {
        //System.out.println("Querying...");
        var ret = new ArrayList<String>();
        var sql = "Select species From populations Where endangered = TRUE";
        if (!DBConnectionManager.connect()) return null;
        DBConnectionManager.beginStatement();
        try {
            var res = DBConnectionManager.executeStatement(sql);
            if (res != null) {
                assert res.metaData.getColumnCount() == 2;
                while (res.resultSet.next()) {
                    var speciesName = res.resultSet.getString(1);
                    // FIX LATER
                    if (speciesName.equals("Endangered")) continue;
                    speciesName = String.format("%25s", speciesName);
                    ret.add(speciesName.trim());
                }
                //System.out.println("Finished!");
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

    public static Population getPopulationFromDB(String forest, String species) {
        if (!DBConnectionManager.connect()) return null;
        Population ret = null;
        var sql = "Select * From Populations Where species=? And fname=?";
        var stmt = DBConnectionManager.prepareStatement(sql);
        if (stmt != null) try {
            stmt.setString(1, species);
            stmt.setString(2, forest);
            var result = DBConnectionManager.executeQuery(stmt);
            if (result == null) return null;
            var cols = result.metaData.getColumnCount();
            if (cols != 5) return null;
            while (result.resultSet.next())
                ret = new Population(
                        result.resultSet.getString(1),
                        result.resultSet.getString(2),
                        result.resultSet.getFloat(3),
                        result.resultSet.getInt(4),
                        result.resultSet.getBoolean(5));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            DBConnectionManager.endStatement();
            DBConnectionManager.disconnect();
            return null;
        }
        DBConnectionManager.disconnect();
        return ret;
    }

    public static ArrayList<Population> getPopulationsInForestFromDB(String forest) {
        if (!DBConnectionManager.connect()) return null;
        var ret = new ArrayList<Population>();
        var sql = "Select * From Populations Where fname=?";
        var stmt = DBConnectionManager.prepareStatement(sql);
        if (stmt != null) try {
            stmt.setString(1, forest);
            var result = DBConnectionManager.executeQuery(stmt);
            if (result == null) return null;
            if (result.metaData.getColumnCount() != 5) return null;
            while (result.resultSet.next()) {
                ret.add(new Population(
                        result.resultSet.getString(1),
                        result.resultSet.getString(2),
                        result.resultSet.getFloat(3),
                        result.resultSet.getInt(4),
                        result.resultSet.getBoolean(5)));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            DBConnectionManager.endStatement();
            DBConnectionManager.disconnect();
            return null;
        }
        DBConnectionManager.disconnect();
        return ret;
    }

    public static PopulationSample getLastSampleBefore(Timestamp time, String species, String fname) {
        if (!DBConnectionManager.connect()) return null;
        var sql = """
                  Select * from populationsamples 
                  Where species=? And fname=?
                  And samplingtime<?
                  Order By samplingtime DESC
                  Limit 1
                  """;
        var stmt = DBConnectionManager.prepareStatement(sql);
        if (stmt == null) return null;
        PopulationSample ret = null;
        try {
            stmt.setString(1, species);
            stmt.setString(2, fname);
            stmt.setTimestamp(3, time);
            var res = DBConnectionManager.executeQuery(stmt);
            if (res == null) {
                return null;
            }
            while (res.resultSet.next())
                ret = new PopulationSample(
                    (UUID)res.resultSet.getObject(1),
                    res.resultSet.getTimestamp(2),
                    res.resultSet.getInt(3),
                    res.resultSet.getString(4).trim(),
                    res.resultSet.getString(5).trim());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            DBConnectionManager.endStatement();
            DBConnectionManager.disconnect();
            return null;
        }
        DBConnectionManager.disconnect();
        return ret;
    }

    public static PopulationSample getFirstSampleAfter(Timestamp time, String species, String fname) {
        if (!DBConnectionManager.connect()) return null;
        var sql = """
                  Select * from populationsamples 
                  Where species=? And fname=?
                  And samplingtime>?
                  Order By samplingtime ASC 
                  Limit 1
                  """;
        var stmt = DBConnectionManager.prepareStatement(sql);
        if (stmt == null) return null;
        PopulationSample ret = null;
        try {
            stmt.setString(1, species);
            stmt.setString(2, fname);
            stmt.setTimestamp(3, time);
            var res = DBConnectionManager.executeQuery(stmt);
            if (res == null) {
                return null;
            }
            while (res.resultSet.next())
                ret = new PopulationSample(
                        (UUID)res.resultSet.getObject(1),
                        res.resultSet.getTimestamp(2),
                        res.resultSet.getInt(3),
                        res.resultSet.getString(4).trim(),
                        res.resultSet.getString(5).trim());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            DBConnectionManager.endStatement();
            DBConnectionManager.disconnect();
            return null;
        }
        DBConnectionManager.disconnect();
        return ret;
    }

    public static boolean addPopulationSampleToDB(PopulationSample popSample){
            if (!DBConnectionManager.connect()) return false;

            var sql = "Insert into populationsamples values (?, ?, ?, ?, ?)";
            var stmt = DBConnectionManager.prepareStatement(sql);
            try {
                stmt.setObject(1, popSample.getSampleID());
                stmt.setTimestamp(2, popSample.getSamplingTime());
                stmt.setInt(3, popSample.getSampleHeadcount());
                stmt.setString(4, popSample.getSpecies());
                stmt.setString(5, popSample.getForestName());
                DBConnectionManager.executeUpdate(stmt);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return false;
            }
            DBConnectionManager.disconnect();
            return true;
    }

    public static ArrayList<String> getForestNameWithForestFires30Days(){
        var queryResult = new ArrayList<String>();
        var sql = "SELECT DISTINCT * From forestfires ff WHERE ff.starttime >= CURRENT_DATE - INTERVAL '30 days'";
        if (!DBConnectionManager.connect()) return null;
        DBConnectionManager.beginStatement();
        try {
            var res = DBConnectionManager.executeStatement(sql);
            if (res != null) {
                assert res.metaData.getColumnCount() == 1;
                while (res.resultSet.next()) {
                    var forestNames = res.resultSet.getString("fname");
                    System.out.println(forestNames);
                    forestNames = String.format("%30s", forestNames);
                    var entry = format(res.resultSet, forestNames, "", 4, 0);
                    queryResult.add(entry);
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
        return queryResult;
    }

    public static ArrayList<ForestFire> getForestFiresFromDB() {
        if (!DBConnectionManager.connect()) return null;
        ArrayList<ForestFire> ret = null;
        var sql = "Select * from forestfires";
        DBConnectionManager.beginStatement();
        try {
            var res = DBConnectionManager.executeStatement(sql);
            if (res != null) {
                ret = new ArrayList<>();
                var cols = res.metaData.getColumnCount();
                if (cols != 4) return null;
                while (res.resultSet.next()) {
                    ret.add(new ForestFire(
                            res.resultSet.getTimestamp(1),
                            res.resultSet.getTimestamp(2),
                            res.resultSet.getFloat(3),
                            res.resultSet.getString(4).trim()
                            ));
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
