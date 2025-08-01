import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class DBInitializer {
    public static void initialize() {
        String url = "jdbc:sqlite:library.db";
        String sql = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                author TEXT NOT NULL,
                year INTEGER
            );
            """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Books table created successfully.");

        } catch (Exception e) {
            System.out.println("Error while creating table: " + e.getMessage());
        }
    }
}
