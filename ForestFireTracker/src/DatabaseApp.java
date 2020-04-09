import java.sql.*;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseApp {

    //Method that returns a connection object to the database
    public static Connection connect(){

        //Parameters for connecting to the database
        final String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        final String password = "FFT1234!";
        final String username = "cs421g12";

        //Connect to the database
        Connection connection = null;

        //Need to register JDBC postgresql driver
        try {
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        } catch (Exception cnfe){
            System.out.println("Class not found");
        }

        try{
            //Try connecting to the database with url,username and password
            connection = DriverManager.getConnection(url, username,password);
            System.out.println("Connected to the PostgreSQL Server Successfully");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    public static void thing() throws SQLException {
        Connection connection = connect();
        System.out.println();

        //Create a blank statement to execute
        Statement statement = connection.createStatement();


        //Main logic loop for asking users what they want to do with our database

        Scanner scanner = new Scanner(System.in);

        System.out.println("Option 0: Quit");
        System.out.println("Option 1: Find surface area by forest name");
        System.out.println("Option 2:  ");



        System.out.println("Enter an option you'd like to execute: ");
        int option = scanner.nextInt();
        System.out.println();

        while(option != 0){


            if(option == 1){

                try{

                    System.out.println("Which forest do you want to enter:  ");
                    System.out.print("Amazon rain forest, Congo basin rain forest, Pike national forest, Cleveland national forest," +
                            "Siberian forests (all)");

                    System.out.println();
                    scanner.nextLine();
                    String forestName = scanner.nextLine();
                    String sqlStatement = ("select * from zones where fname = ? ");

                    PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                    preparedStatement.setString(1, forestName);
                    //preparedStatement.executeUpdate();

                    java.sql.ResultSet resultSet = preparedStatement.executeQuery();
                    java.sql.ResultSetMetaData metaData = resultSet.getMetaData();

                    int columnsNumber = metaData.getColumnCount();

                    while(resultSet.next()){

                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = resultSet.getString(i);
                            System.out.print(metaData.getColumnName(i) + ": " + columnValue);
                        }
                        System.out.println("");
                    }


                }catch(SQLException se){
                    int count = 1;
                    while (se != null) {
                        System.out.println("SQLException " + count);
                        System.out.println("Code: " + se.getErrorCode());
                        System.out.println("SqlState: " + se.getSQLState());
                        System.out.println("Error Message: " + se.getMessage());
                        se = se.getNextException();
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



            } //End of option 1

            if(option == 2){
                System.out.println("lol2");

            }

            System.out.println("Enter an option you'd like to execute: ");
            option = scanner.nextInt();

        }

        System.out.println("Program ended");



        statement.close();
        connection.close();


        System.out.println("STOP");
    }

    public static void main(String[] args) {
        // TODO: parse args???
        var forest_mgmt = new CliMenu("Forest Management Menu", CliMenu.menuType.SUB);
        forest_mgmt.addOption("List forests", () -> {});
        forest_mgmt.addOption("List forests", () -> {});
        forest_mgmt.addOption("List forests", () -> {});

        var main_menu = new CliMenu("Main Menu", CliMenu.menuType.MAIN);
        main_menu.addOption("Manage Forests", forest_mgmt::execute);
        main_menu.addOption("Manage Populations", () -> {});
        main_menu.addOption("Manage A thing", () -> {});
        main_menu.addOption("thing 1", () -> {});
        main_menu.addOption("thing 2", () -> {});
        main_menu.execute();
    }
}


