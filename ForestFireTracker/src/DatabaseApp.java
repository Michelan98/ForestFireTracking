public class DatabaseApp {
    public static void main(String[] args) {
        // TODO: parse args???
        var forest_mgmt = new CliMenu("Forest Management Menu", CliMenu.menuType.SUB);
        forest_mgmt.addOption("List forests", () -> {
            var forests = QueryManager.getForestNamesFromDB();
            if (forests != null) {
                System.out.println();
                for (var f : forests) {
                    System.out.println(f);
                }
                System.out.println();
            } else {
                System.err.format("Unable to retrieve forest names.\n\n");
            }
        });
        forest_mgmt.addOption("Get surface area of a forest", ()-> {
            var forestSurfaceAreas = QueryManager.getForestSurfaceAreas();

            if (forestSurfaceAreas != null) {
                System.out.println();
                for (var f : forestSurfaceAreas) {
                    System.out.println(f);
                }
                System.out.println();
            } else {
                System.err.format("Unable to retrieve forest names.\n\n");
            }
        });
        forest_mgmt.addOption("List ongoing forest fires", () -> {/*TODO*/});

        var pop_mgmt = new CliMenu("Population Management Menu", CliMenu.menuType.SUB);
        pop_mgmt.addOption("List all species", () -> {
            var species = QueryManager.getSpeciesNamesFromDB();
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


