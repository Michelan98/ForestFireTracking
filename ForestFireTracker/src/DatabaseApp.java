import java.sql.*;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseApp {
    public static void main(String[] args) {
        // TODO: parse args???
        var forest_mgmt = new CliMenu("Forest Management Menu", CliMenu.menuType.SUB);
        forest_mgmt.addOption("List forests", () -> {/*TODO*/});
        forest_mgmt.addOption("Get surface area of a forest", () -> {/*TODO*/});
        forest_mgmt.addOption("List ongoing forest fires", () -> {/*TODO*/});

        var pop_mgmt = new CliMenu("Population Management Menu", CliMenu.menuType.SUB);
        pop_mgmt.addOption("List all species", () -> {/*TODO*/});
        pop_mgmt.addOption("List endangered species", () -> {/*TODO*/});
        pop_mgmt.addOption("Add population sample", () -> {/* TODO: create a class to handle that with an argument parser */});

        var fire_mgmt = new CliMenu("Forest Fire Management Menu", CliMenu.menuType.SUB);
        fire_mgmt.addOption("Get population impact of a fire", () -> {/*TODO*/});

        var main_menu = new CliMenu("Main Menu", CliMenu.menuType.MAIN);
        main_menu.addOption("Manage Forests", forest_mgmt::execute);
        main_menu.addOption("Manage Populations", pop_mgmt::execute);
        main_menu.addOption("Manage Forest Fires", fire_mgmt::execute);
        main_menu.execute();
    }
}


