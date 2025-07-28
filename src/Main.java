 import java.util.ArrayList;
 import java.util.Scanner;


    public class Main {
        public static void main(String[] args) {
            DBInitializer.initialize();


            Library library = new Library();
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            System.out.println(" Welcome to Book Management System!");
            MenuManager.pressEnterToContinue();

            while (running) {
                System.out.println();
                System.out.println("What do you want to do?");
                System.out.println("1. Add a book");
                System.out.println("2. Remove a book");
                System.out.println("3. List all books");
                System.out.println("4. Search for book");
                System.out.println("5. Exit");
                System.out.print("Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                switch (choice) {
                    case 1:
                        int id = 0;
                        System.out.println("Enter book name:");
                        String name = scanner.nextLine();

                        System.out.println("Enter author name:");
                        String author = scanner.nextLine();

                        System.out.println("Enter published year:");
                        int year = scanner.nextInt();
                        scanner.nextLine();

                        library.addBook(new Books(id, name, author, year));

                        System.out.println("\nAll the books in the library:");
                        library.getAllBooks();
                        break;

                    case 2:
                        System.out.print("Enter the book name you want to remove: ");
                        String nameToRemove = scanner.nextLine();

                        boolean exists = library.bookExists(nameToRemove);
                        if (!exists) {
                            System.out.println("Book named \"" + nameToRemove + "\" not found in library.");
                        } else {
                            System.out.print("Are you sure you want to delete this book? (y/n): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equalsIgnoreCase("y")) {
                                boolean removed = library.removeBook(nameToRemove);
                                if (removed) {
                                    System.out.println(nameToRemove + " removed from library.");
                                }
                            } else {
                                System.out.println("Book removal cancelled.");
                            }
                        }
                        break;

                    case 3:
                        library.getAllBooks();
                        break;

                    case 4:
                        System.out.print("Name of the book you want to search: ");
                        String searchName = scanner.nextLine();
                        ArrayList<Books> results = library.searchBooksByName(searchName);

                        if (results.isEmpty()) {
                            System.out.println("Book not found.");
                        } else {
                            System.out.println("Related books:");
                            for (Books book : results) {
                                System.out.println(book.getId() + " || " + book.getName() + " || " +
                                                   book.getAuthor() + " || " + book.getPublishedYear());
                            }
                        }
                        break;

                    case 5:
                        MenuManager.showInfo("Exiting system...");
                        MenuManager.showSuccess("Thank you for using Library Management System!");
                        running = false;
                        break;

                    default:

                }
            }

        }


    }