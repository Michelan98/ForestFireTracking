import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseApp {
    static ArrayList<String> forests_cache = null;
    static ArrayList<String> species_cache = null;

    public static void addPopSample() {
        // Get the forests and species and use for entering samples
        if (forests_cache == null) {
            forests_cache = QueryManager.getForestNamesFromDB();
        }
        if (species_cache == null) {
            species_cache = QueryManager.getSpeciesNamesFromDB();
        }

        // Do the thing
        var scanner = new Scanner(System.in);
        var forest = "";
        var species = "";
        System.out.format("\t%s\n", "Population Sample Entry Tool");
        System.out.println("Enter 'cancel' to quit tool");
        System.out.print("Enter forest name > ");
        var input = scanner.nextLine();
        if (input.equals("cancel")) return;
        while (!forests_cache.contains(input)) {
            System.out.format("Forests database does not contain any forest with name: %s\n", input);
            System.out.println("Enter an existing forest or leave and add forest to forest database.");
            System.out.print("Enter forest name > ");
            input = scanner.nextLine();
            if (input.equals("cancel")) return;
        }
        forest = input;
        // Valid forest name entered, enter species
        System.out.print("Enter species name > ");
        input = scanner.nextLine();
        if (input.equals("cancel")) return;
        while (!species_cache.contains(input)) {
            System.out.format("Animals database does not contain any species with name: %s\n", input);
            System.out.println("Enter an existing species or leave and add species to animal database.");
            System.out.print("Enter species name > ");
            input = scanner.nextLine();
            if (input.equals("cancel")) return;
        }
        species = input;
        // Both valid forest and species entered, validate that a population exists
        var pop = QueryManager.getPopulationFromDB(forest, species);
        // sanity check, there should be only one population per forest/species pair
        if (pop == null || pop.isEmpty() || !pop.contains(species) || !pop.contains(forest)) {
            System.err.format("""
                            There is no population of %s in %s
                            Or the connection could not be established.
                            Add a new population and then start collecting samples.
                            """, species, forest);
            return;
        }

        var headCount = 0;
        var sampleTime = LocalDateTime.now();
        var sampleId = UUID.randomUUID();
        var tempCount = -1;
        while (tempCount < 0) {
            System.out.print("Enter headcount > ");
            tempCount = scanner.nextInt();
            if (tempCount < 0) {
                System.out.println("Please enter a non-negative value (or 0).");
            }
        }
        headCount = tempCount;
        if (QueryManager.addPopulationSampleToDB(sampleId, sampleTime, headCount, species, forest)) {
            System.out.println("Successfully added sample to the database!");
        } else {
            System.out.println("Failed to add sample to the database!");
        }
    }

    public static void main(String[] args) {
        var forest_mgmt = new CliMenu("Forest Management Menu", CliMenu.menuType.SUB);
        forest_mgmt.addOption("List forests", () -> {
            var forests = QueryManager.getForestNamesFromDB();
            if (forests != null) {
                forests_cache = forests;
                System.out.println();
                for (var f : forests) {
                    System.out.println(f);
                }
                System.out.println();
            } else {
                System.err.format("Unable to retrieve forest names.\n\n");
            }
        });
        forest_mgmt.addOption("Get surface area of a forest", () -> {/*TODO*/});
        forest_mgmt.addOption("List ongoing forest fires", () -> {/*TODO*/});
        forest_mgmt.addExit();

        var pop_mgmt = new CliMenu("Population Management Menu", CliMenu.menuType.SUB);
        pop_mgmt.addOption("List all species", () -> {
            var species = QueryManager.getSpeciesNamesFromDB();
            if (species != null) {
                species_cache = species;
                System.out.println();
                for (var s : species) {
                    System.out.println(s);
                }
                System.out.println();
            } else {
                System.err.format("Unable to retrieve species.\n\n");
            }
        });
        pop_mgmt.addOption("List endangered species", () -> {
            var species = QueryManager.getEndangeredSpeciesFromDB();
            if (species != null) {
                System.out.println();
                for (var s : species) {
                    System.out.println(s);
                }
                System.out.println();
            } else {
                System.err.format("Unable to retrieve species.\n\n");
            }
        });
        pop_mgmt.addOption("Add population sample", DatabaseApp::addPopSample);
        pop_mgmt.addExit();

        var fire_mgmt = new CliMenu("Forest Fire Management Menu", CliMenu.menuType.SUB);
        fire_mgmt.addOption("Get population impact of a fire", () -> {/*TODO*/});
        fire_mgmt.addExit();

        var main_menu = new CliMenu("Main Menu", CliMenu.menuType.MAIN);
        main_menu.addOption("Manage Forests", forest_mgmt::execute);
        main_menu.addOption("Manage Populations", pop_mgmt::execute);
        main_menu.addOption("Manage Forest Fires", fire_mgmt::execute);
        main_menu.addExit();
        main_menu.execute();
    }
}


