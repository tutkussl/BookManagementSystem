import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LibraryGUI extends Application {

    private Library library;
    private TableView<Books> bookTable;
    private TextField nameField, authorField, yearField, searchField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        DBInitializer.initialize();
        library = new Library();

        primaryStage.setTitle("MyLibrary");
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);

        BorderPane mainLayout = new BorderPane();

        Label titleLabel = new Label("Book Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(15));
        titleBox.setStyle("-fx-background-color: #ba96c1;");

        createBookTable();
        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        statusLabel = new Label("System is ready.");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        HBox statusBox = new HBox(statusLabel);
        statusBox.setPadding(new Insets(10));
        statusBox.setStyle("-fx-background-color: #ecf0f1;");

        mainLayout.setTop(titleBox);
        mainLayout.setCenter(bookTable);
        mainLayout.setLeft(leftPanel);
        mainLayout.setRight(rightPanel);
        mainLayout.setBottom(statusBox);

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        refreshTable();
    }

    private void createBookTable() {
        bookTable = new TableView<>();

        TableColumn<Books, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Books, String> nameColumn = new TableColumn<>("Book name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(250);

        TableColumn<Books, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(200);

        TableColumn<Books, Integer> yearColumn = new TableColumn<>("Publication Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publishedYear"));
        yearColumn.setPrefWidth(80);

        bookTable.getColumns().addAll(idColumn, nameColumn, authorColumn, yearColumn);
        bookTable.setStyle("-fx-selection-bar: #706f7e; -fx-selection-bar-non-focused: #bdc3c7;");
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(200);
        leftPanel.setStyle("-fx-background-color: #f8f9fa;");

        Label leftTitle = new Label("Actions");
        leftTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label searchLabel = new Label("Search a book:");
        searchField = new TextField();
        searchField.setPromptText("Enter a book name");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #3e1e36; -fx-text-fill: white;");
        searchButton.setPrefWidth(150);
        searchButton.setOnAction(e -> searchBooks());

        Button showAllButton = new Button("List all books");
        showAllButton.setStyle("-fx-background-color: #733863; -fx-text-fill: white;");
        showAllButton.setPrefWidth(150);
        showAllButton.setOnAction(e -> refreshTable());

        Label sortLabel = new Label("Sort by:");
        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("By ID", "Alphabetical (A-Z)", "Alphabetical (Z-A)", "By author name");
        sortComboBox.setValue("By ID");
        sortComboBox.setPrefWidth(150);

        sortComboBox.setOnAction(e -> {
            String selectedSort = sortComboBox.getValue();
            sortBooks(selectedSort);
        });

        Button removeButton = new Button("Delete selected book");
        removeButton.setStyle("-fx-background-color: #ae739c; -fx-text-fill: white;");
        removeButton.setPrefWidth(150);
        removeButton.setOnAction(e -> removeSelectedBook());

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #dcd7d5; -fx-text-fill: #090e0e;");
        exitButton.setPrefWidth(150);
        exitButton.setOnAction(e -> System.exit(0));

        leftPanel.getChildren().addAll(
                leftTitle,
                new Separator(),
                searchLabel, searchField, searchButton,
                showAllButton,
                sortLabel, sortComboBox,
                new Separator(),
                removeButton,
                new Separator(),
                exitButton
        );

        return leftPanel;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(15));
        rightPanel.setPrefWidth(250);
        rightPanel.setStyle("-fx-background-color: #f8f9fa;");

        Label rightTitle = new Label("Add a new book");
        rightTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label nameLabel = new Label("Book name:");
        nameField = new TextField();
        nameField.setPromptText("Enter a book name");

        Label authorLabel = new Label("Author:");
        authorField = new TextField();
        authorField.setPromptText("Enter authors name");

        Label yearLabel = new Label("Published Year:");
        yearField = new TextField();
        yearField.setPromptText("Enter a year (ex.:2020");

        Button addButton = new Button("Add book");
        addButton.setStyle("-fx-background-color: #6c5f8d; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setPrefWidth(200);
        addButton.setOnAction(e -> addBook());

        Button clearButton = new Button("Clear all");
        clearButton.setStyle("-fx-background-color: #9c8cb9; -fx-text-fill: white;");
        clearButton.setPrefWidth(200);
        clearButton.setOnAction(e -> clearForm());

        rightPanel.getChildren().addAll(
                rightTitle,
                new Separator(),
                nameLabel, nameField,
                authorLabel, authorField,
                yearLabel, yearField,
                new Separator(),
                addButton,
                clearButton
        );

        return rightPanel;
    }

    private void addBook() {
        try {
            String name = nameField.getText().trim();
            String author = authorField.getText().trim();
            String yearText = yearField.getText().trim();

            if (name.isEmpty() || author.isEmpty() || yearText.isEmpty()) {
                showAlert("Error", "Please fill in all sections ", Alert.AlertType.ERROR);
                return;
            }

            int year = Integer.parseInt(yearText);
            Books newBook = new Books(0, name, author, year);
            library.addBook(newBook);

            refreshTable();
            clearForm();
            updateStatus("Book added successfully: " + name);

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number!", Alert.AlertType.ERROR);
        }
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }

        ArrayList<Books> results = library.searchBooksByName(searchTerm);
        bookTable.getItems().clear();
        bookTable.getItems().addAll(results);

        updateStatus(results.size() + " book found.");
    }

    private void removeSelectedBook() {
        Books selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Warning", "Please select the book you want to delete!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cornfirmation");
        confirmation.setHeaderText("Are you sure you want to delete this book?");
        confirmation.setContentText("Book: " + selectedBook.getName());

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            library.removeBook(selectedBook.getName());
            refreshTable();
            updateStatus("Book deleted: " + selectedBook.getName());
        }
    }

    private void refreshTable() {
        bookTable.getItems().clear();
        bookTable.getItems().addAll(library.getAllBooks());
        updateStatus("Table updated. Total " + library.getAllBooks().size() + " books.");
    }

    private void sortBooks(String sortType) {
        ArrayList<Books> allBooks = library.getAllBooks();

        switch (sortType) {
            case "By ID":
                allBooks.sort((b1, b2) -> Integer.compare(b1.getId(), b2.getId()));
                break;
            case "Alphabetical (A-Z)":
                allBooks.sort((b1, b2) -> b1.getName().compareToIgnoreCase(b2.getName()));
                break;
            case "Alphabetical (Z-A)":
                allBooks.sort((b1, b2) -> b2.getName().compareToIgnoreCase(b1.getName()));
                break;
            case "By author name":
                allBooks.sort((b1, b2) -> b1.getAuthor().compareToIgnoreCase(b2.getAuthor()));
                break;
        }

        bookTable.getItems().clear();
        bookTable.getItems().addAll(allBooks);
        updateStatus("Books sorted by: " + sortType);
    }

    private void clearForm() {
        nameField.clear();
        authorField.clear();
        yearField.clear();
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}