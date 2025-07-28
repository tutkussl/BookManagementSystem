public class MenuManager {

    private static final String MENU_HEADER =
            "\n" + "=".repeat(65) + "\n" +
            "             BOOK MANAGEMENT SYSTEM\n" +
            "=".repeat(65);

    private static final String MAIN_MENU_OPTIONS =
            "\n🔹 1. Add a book\n" +
            "\n🔹 2. Delete a book\n" +
            "\n🔹 3. List all books\n" +
            "\n🔹 4. Search for a book\n" +
            "\n🔹 5. Exit the program\n" +
            "\n" + "=".repeat(65) +
            "\n Make a choise (0-6): ";

    public static void displayMainMenu() {
        clearScreen();
        System.out.println(MENU_HEADER);
        System.out.print(MAIN_MENU_OPTIONS);
    }

    public static void displaySearchMenu() {
        clearScreen();
        System.out.println("\nSearch options:");
        System.out.println("=" .repeat(30));
        System.out.println("1. Başlığa Göre Ara");
        System.out.println("2. Yazara Göre Ara");
        System.out.println("3. ISBN'e Göre Ara");
        System.out.println("0. Return to main menu");
        System.out.println("=" .repeat(30));
        System.out.print("Your choice: ");
    }

    public static void displayListMenu() {
        clearScreen();
        System.out.println("\n Listing choices");
        System.out.println("=" .repeat(35));
        System.out.println("1. List all books ");
        System.out.println("2. Alphabetical order (A-Z)");
        System.out.println("3. Alphabetical order (Z-A)");
        System.out.println("0. Return to menu");
        System.out.println("=" .repeat(35));
        System.out.print("Your choice: ");
    }

    public static void showSuccess(String message) {
        System.out.println("\n " + message);
    }

    public static void showError(String message) {
        System.out.println("\n " + message);
    }

    public static void showInfo(String message) {
        System.out.println("\n " + message);
    }

    public static void showWarning(String message) {
        System.out.println("\n️  " + message);
    }

    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void pressEnterToContinue() {
        System.out.print("\n⏸Press enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    public static void showTitle(String title) {
        System.out.println("\n" + "=".repeat(title.length() + 10));
        System.out.println("     " + title.toUpperCase());
        System.out.println("=".repeat(title.length() + 10));
    }

    public static void showSeparator() {
        System.out.println("-".repeat(50));
    }
}