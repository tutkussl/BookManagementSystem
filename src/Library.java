import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Library {
    private ArrayList<Books> books = new ArrayList<>();
    private int nextId = 1;
    private final String FILE_PATH = "books.json";

    public Library() {
        loadBooks();
        if (books.isEmpty()) {
            addBook(new Books(0, "To Kill a Mockingbird", "Harper Lee", 1960));
            addBook(new Books(0, "Alchemist", "Paulo Coelho", 1988));
            addBook(new Books(0, "The Da Vinci Code", "Dan Brown", 2003));
            addBook(new Books(0, "Harry Potter and the Deathly Hallows", "J. K. Rowling", 2007));
        }

        for (Books b : books) {
            if (b.getId() >= nextId) {
                nextId = b.getId() + 1;
            }
        }
    }

    public void addBook(Books book) {
        String trimmedName = (book.getName() != null) ? book.getName().trim() : "";
        if (trimmedName.isEmpty() || trimmedName.length() < 2) return;

        boolean exists = books.stream().anyMatch(b -> b.getName().trim().equalsIgnoreCase(trimmedName));
        if (!exists) {
            if (book.getId() == 0) book.setId(nextId++);
            books.add(book);
            saveBooks();
            System.out.println(book.getId() + ": \"" + trimmedName + "\" added to the library.");
        }
    }

    public boolean removeBook(String name) {
        String trimmedName = (name != null) ? name.trim() : "";
        Optional<Books> bookToRemove = books.stream()
                .filter(book -> book.getName().trim().equalsIgnoreCase(trimmedName))
                .findFirst();

        if (bookToRemove.isPresent()) {
            books.remove(bookToRemove.get());
            saveBooks();
            System.out.println("\"" + trimmedName + "\" removed from the library.");
            return true;
        }
        return false;
    }

    public ArrayList<Books> getAllBooks() {
        return new ArrayList<>(books);
    }

    public boolean addBookGUI(Books book) {
        String trimmedName = (book.getName() != null) ? book.getName().trim() : "";
        if (trimmedName.isEmpty() || trimmedName.length() < 2) return false;

        boolean exists = books.stream().anyMatch(b -> b.getName().trim().equalsIgnoreCase(trimmedName));
        if (!exists) {
            if (book.getId() == 0) book.setId(nextId++);
            books.add(book);
            saveBooks();
            return true;
        }
        return false;
    }

    public boolean removeBookGUI(String name) {
        boolean result = removeBook(name);
        if (result) saveBooks();
        return result;
    }

    public ArrayList<Books> searchBooksByName(String name) {
        String trimmedName = (name != null) ? name.trim().toLowerCase() : "";
        ArrayList<Books> foundBooks = new ArrayList<>();
        for (Books book : books) {
            if (book.getName().toLowerCase().contains(trimmedName)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    public boolean bookExists(String name) {
        String trimmedName = (name != null) ? name.trim() : "";
        return books.stream()
                .anyMatch(book -> book.getName().trim().equalsIgnoreCase(trimmedName));
    }

    private void saveBooks() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(FILE_PATH), books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                books = mapper.readValue(file, new TypeReference<ArrayList<Books>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
