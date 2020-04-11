import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CliMenu {
    public enum menuType {
        MAIN, SUB
    }

    static class MenuEntry {
        private final String name;
        private final Runnable func;

        public MenuEntry(String name, Runnable func) {
            this.name = name;
            this.func = func;
        }

        public String getName() { return this.name; }
        public void call() { func.run(); }
    }

    private final ArrayList<MenuEntry> options;
    private final String menuName;
    private final menuType type;
    private boolean loop;

    public CliMenu(String menuName, menuType type) {
        this.options = new ArrayList<>();
        this.menuName = menuName;
        this.type = type;
        this.loop = true;
    }

    public void addOption(String name, Runnable func) {
        this.options.add(new MenuEntry(name, func));
    }

    public void addExit() {
        options.add(switch (type) {
            case MAIN -> new MenuEntry("Exit", () -> System.exit(0));
            case SUB -> new MenuEntry("Back", () -> this.loop = false);
        });
    }

    public void execute() {
        this.loop = true;
        var scanner = new Scanner(System.in);
        // Add Exit options at the end if main menu, add "go back" if sub
        // Loop as long as you wish
        while (this.loop) {
            var i = 0;
            System.out.format("\t%s\n", this.menuName);
            for (var opt : this.options) {
                System.out.format("[%d] -\t%s\n", i++, opt.getName());
            }
            System.out.format("Choice: ");
            var option = scanner.nextInt();
            if (option >= 0 && option < options.size()) {
                options.get(option).call();
            } else {
                System.out.println("Invalid option!\n");
            }
            System.out.println();
        }
    }
}
