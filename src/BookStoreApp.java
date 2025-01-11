import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Oracle JDBC Driver import
import oracle.jdbc.OracleDriver;

public class BookStoreApp {
    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private List<Book> books;
    private List<CartItem> cartItems;
    private Connection dbConnection;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new BookStoreApp());
    }

    public BookStoreApp() {
        cartItems = new ArrayList<>();
        books = new ArrayList<>();
        initializeUI();

        try {
            connectToDatabase(); // Establish database connection
            loadBooksFromDatabase(); // Load books initially
            updateTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void connectToDatabase() throws SQLException {
        try {
            DriverManager.registerDriver(new OracleDriver());
            dbConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", // Update with your DB URL
                "username", // Update with your username
                "password"  // Update with your password
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. Check your connection settings.", "Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    private void initializeUI() {
        frame = new JFrame("Book Store App");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top Panel with search and cart buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnViewCart = new JButton("View Cart");
        btnViewCart.addActionListener(e -> viewCart());
        topPanel.add(btnViewCart);

        JTextField searchField = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks(searchField.getText()));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(btnSearch);

        frame.add(topPanel, BorderLayout.NORTH);

        // Book Table
        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "ISBN", "Quantity", "Price", "Add to Cart"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only "Add to Cart" column is editable
            }
        };
        bookTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private List<Book> loadBooksFromDatabase() throws SQLException {
        List<Book> loadedBooks = new ArrayList<>();
        String query = "SELECT title, author, isbn, quantity, price FROM books";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                loadedBooks.add(book);
            }
        }
        books = loadedBooks;
        return loadedBooks;
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getQuantity(),
                book.getPrice(),
                "Add to Cart"
            });
        }
    }

    private void searchBooks(String searchTerm) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase()) ||
                book.getIsbn().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        books = filteredBooks;
        updateTable();
    }

    private void viewCart() {
        StringBuilder cartDetails = new StringBuilder("Cart Items:\n");
        double total = 0;

        for (CartItem item : cartItems) {
            cartDetails.append(item.getBook().getTitle()).append(" - Qty: ")
                .append(item.getQuantity()).append(" - $").append(item.getBook().getPrice()).append("\n");
            total += item.getBook().getPrice() * item.getQuantity();
        }

        cartDetails.append("\nTotal: $").append(total);

        JOptionPane.showMessageDialog(frame, cartDetails.toString(), "Your Cart", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            dbConnection.setAutoCommit(false); // Start transaction

            reduceBookQuantitiesInDatabase();
            saveTransactionDetailsToDatabase();
            dbConnection.commit(); // Commit transaction

            JOptionPane.showMessageDialog(frame, "Checkout successful!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
            cartItems.clear(); // Clear the cart
        } catch (SQLException e) {
            try {
                dbConnection.rollback(); // Rollback transaction in case of error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "Checkout failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                dbConnection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void reduceBookQuantitiesInDatabase() throws SQLException {
        String query = "UPDATE books SET quantity = quantity - ? WHERE isbn = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            for (CartItem item : cartItems) {
                stmt.setInt(1, item.getQuantity());
                stmt.setString(2, item.getBook().getIsbn());
                stmt.executeUpdate();
            }
        }
    }

    private void saveTransactionDetailsToDatabase() throws SQLException {
        String query = "INSERT INTO transactions (transaction_id, transaction_date, total_cost) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, "TXN" + System.currentTimeMillis());
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setDouble(3, cartItems.stream().mapToDouble(item -> item.getBook().getPrice() * item.getQuantity()).sum());
            stmt.executeUpdate();
        }
    }
}
