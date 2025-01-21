--- START OF FILE DatabaseUtils.java ---
import java.sql.*; // Imports the necessary classes for JDBC (Java Database Connectivity).
import oracle.jdbc.driver.OracleDriver; // Imports the specific Oracle JDBC driver class.
import javax.swing.*; // Imports classes for creating graphical user interfaces (used for error messages).

public class DatabaseUtils {

    public static Connection getConnection() throws SQLException {
        // This method establishes a connection to the Oracle database. It might throw an SQLException if something goes wrong.
        try {
            String jdbcUrl = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
            // Defines the JDBC URL (Uniform Resource Locator) which specifies how to connect to the database.
            // - "jdbc:oracle:thin:" indicates the Oracle thin driver is used.
            // - "@127.0.0.1" is the hostname or IP address of the database server (localhost in this case).
            // - "1521" is the port number on which the Oracle database listener is running.
            // - "xe" is the Oracle System Identifier (SID) or service name of the database instance.
            String username = "System"; // The username for connecting to the Oracle database.
            String password = "sysmain123"; // The password for the specified username.

            DriverManager.registerDriver(new OracleDriver());
            // Registers the Oracle JDBC driver with the DriverManager.
            // Think of the DriverManager as the 'parent' and the OracleDriver as a specific 'child' who knows how to talk to Oracle databases.
            // When you ask the parent (DriverManager) for a connection, it uses the right child (OracleDriver) based on the JDBC URL.
            return DriverManager.getConnection(jdbcUrl, username, password);
            // Attempts to establish a connection to the database using the provided URL, username, and password.
            // This is like the parent (DriverManager) telling the child (OracleDriver), "Hey, connect to this place with these credentials!".
            // If the child is successful, it returns a Connection object, which is like a direct line to the database.
        } catch (SQLException e) {
            // Catches any SQLException that might occur during the connection process.
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Displays an error message dialog to the user if the connection fails.
            throw e; // Re-throws the SQLException to be handled by the calling method.
        }
    }
}
--- START OF FILE Admin.java ---
import javax.swing.*; // Imports classes for creating graphical user interfaces.
import javax.swing.table.DefaultTableModel; // Imports the class for creating a default table model.
import javax.swing.table.TableCellRenderer; // Imports the interface for custom cell rendering in tables.
import java.awt.*; // Imports classes for AWT (Abstract Window Toolkit) for GUI components.
import java.awt.event.ActionEvent; // Imports the class for handling action events (like button clicks).
import java.awt.event.ActionListener; // Imports the interface for handling action listeners.
import java.sql.*; // Imports the necessary classes for JDBC (Java Database Connectivity).
import java.util.ArrayList; // Imports the ArrayList class for creating dynamic lists.
import java.util.List; // Imports the List interface for working with lists.

public class Admin {
    private JFrame frame; // The main window of the application.
    private JTable bookTable; // The table to display book information.
    private DefaultTableModel tableModel; // The model that holds the data for the bookTable.
    private JTextField searchField; // The text field for searching books.
    private List<Book> books; // A list to store Book objects retrieved from the database.
    private Connection dbConnection; // Stores the database connection object.

    public static void main(String[] args) {
        // The main method to start the Admin application.
        EventQueue.invokeLater(() -> new Admin()); // Ensures the GUI is created and updated on the Event Dispatch Thread.
    }

    public Admin() {
        // Constructor for the Admin class.
        books = new ArrayList<>(); // Initializes the list of books.
        try {
            // Attempts to establish a database connection.
            dbConnection = DatabaseUtils.getConnection(); // Calls the getConnection method from DatabaseUtils to get a connection.
            // This is like saying, "Hey DatabaseUtils, get me connected to the database!".
            // The getConnection method acts like a helper to establish the connection.
            initializeUI(); // Initializes the user interface components.
            loadBooksFromDatabase(); // Loads book data from the database.
            updateTable(); // Updates the table with the loaded book data.
        } catch (SQLException e) {
            // Catches any SQLException that occurs during database connection or interaction.
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage()); // Displays an error message to the user.
            e.printStackTrace(); // Prints the stack trace of the exception for debugging.
        }
    }

    // Add this method to properly close connection when Admin window closes
    private void cleanup() {
        // This method is responsible for closing the database connection.
        if (dbConnection != null) {
            // Checks if the connection is not null (meaning a connection was established).
            try {
                dbConnection.close(); // Closes the database connection. This is like saying goodbye to the database.
            } catch (SQLException e) {
                e.printStackTrace(); // Prints the stack trace if closing the connection fails.
            }
        }
    }

    private void initializeUI() {
        // Initializes the graphical user interface components.
        frame = new JFrame("Book Store Admin"); // Creates the main frame with the title "Book Store Admin".
        // Add window listener to cleanup when closing
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cleanup(); // Calls the cleanup method when the window is closing.
            }
        });
        frame.setBounds(100, 100, 900, 600); // Sets the initial position and size of the frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sets the default close operation to exit the application.
        frame.getContentPane().setLayout(new BorderLayout(0, 0)); // Sets the layout manager for the frame's content pane.

        JPanel topPanel = new JPanel(new BorderLayout()); // Creates a panel for the top part of the UI.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Creates a panel for buttons with left alignment.

        JButton btnAddBook = new JButton("Add Book"); // Creates the "Add Book" button.
        btnAddBook.addActionListener(e -> addBook()); // Adds an action listener to the "Add Book" button.
        buttonPanel.add(btnAddBook); // Adds the "Add Book" button to the button panel.

        JLabel lblSearch = new JLabel("Search:"); // Creates the "Search:" label.
        searchField = new JTextField(15); // Creates the text field for search input.
        JButton btnSearch = new JButton("Search"); // Creates the "Search" button.
        btnSearch.addActionListener(e -> searchBooks(dbConnection)); // Adds an action listener to the "Search" button.
        buttonPanel.add(lblSearch); // Adds the "Search:" label to the button panel.
        buttonPanel.add(searchField); // Adds the search text field to the button panel.
        buttonPanel.add(btnSearch); // Adds the "Search" button to the button panel.

        topPanel.add(buttonPanel, BorderLayout.WEST); // Adds the button panel to the top panel on the left.

        JButton btnLogout = new JButton("Logout"); // Creates the "Logout" button.
        btnLogout.addActionListener(e -> {
            frame.dispose(); // Closes the current admin window.
            Welcome.main(new String[]{}); // Opens the Welcome window.
        });
        topPanel.add(btnLogout, BorderLayout.EAST); // Adds the "Logout" button to the top panel on the right.
        frame.getContentPane().add(topPanel, BorderLayout.NORTH); // Adds the top panel to the frame at the top.

        tableModel = new DefaultTableModel(
                new Object[][]{}, // Initial data is empty.
                new String[]{"Title", "Author", "ISBN", "Quantity", "Price", "Edit", "Remove"} // Column headers.
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Only "Edit" and "Remove" columns are editable.
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class; // All columns contain String data for display purposes.
            }
        };

        bookTable = new JTable(tableModel); // Creates the book table with the defined table model.
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer()); // Sets a custom renderer for the "Edit" button column.
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit", this)); // Sets a custom editor for the "Edit" button column.
        bookTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer()); // Sets a custom renderer for the "Remove" button column.
        bookTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove", this)); // Sets a custom editor for the "Remove" button column.

        JScrollPane scrollPane = new JScrollPane(bookTable); // Creates a scroll pane for the book table.
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER); // Adds the scroll pane to the frame in the center.

        JButton btnViewSales = new JButton("View Sales"); // Creates the "View Sales" button.
        btnViewSales.addActionListener(e -> viewSales(dbConnection)); // Adds an action listener to the "View Sales" button.
        frame.getContentPane().add(btnViewSales, BorderLayout.SOUTH); // Adds the "View Sales" button to the frame at the bottom.

        frame.setVisible(true); // Makes the frame visible.
    }

    private void loadBooksFromDatabase() {
        // Loads book data from the database and populates the 'books' list.
        books.clear(); // Clears the existing list of books.
        String sql = "SELECT * FROM books ORDER BY title"; // SQL query to select all books, ordered by title.
        // This is a Read operation (R) in CRUD.
        PreparedStatement stmt = null; // Prepares a SQL statement for execution.
        ResultSet rs = null; // Stores the results returned by the SQL query.
        try {
            stmt = dbConnection.prepareStatement(sql); // Creates a prepared statement from the SQL query.
            // Prepared statements are important for security (prevent SQL injection) and efficiency.
            rs = stmt.executeQuery(); // Executes the SQL query and gets the result set.
            while (rs.next()) {
                // Iterates through each row in the result set.
                books.add(new Book(
                        rs.getString("title"), // Retrieves the book title from the current row.
                        rs.getString("author"), // Retrieves the author name.
                        rs.getString("isbn"), // Retrieves the ISBN.
                        rs.getInt("quantity"), // Retrieves the quantity.
                        rs.getDouble("price") // Retrieves the price.
                ));
            }
        } catch (SQLException e) {
            // Handles any SQL exceptions that might occur.
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null); // Closes the database resources (ResultSet, PreparedStatement).
        }
    }

    private void addBook() {
        // Allows the admin to add a new book to the database.
        JTextField titleField = new JTextField(); // Creates a text field for the book title.
        JTextField authorField = new JTextField(); // Creates a text field for the author.
        JTextField isbnField = new JTextField(); // Creates a text field for the ISBN.
        JTextField quantityField = new JTextField(); // Creates a text field for the quantity.
        JTextField priceField = new JTextField(); // Creates a text field for the price.

        Object[] message = {
                // Creates an array of objects to be displayed in the input dialog.
                "Title:", titleField,
                "Author:", authorField,
                "ISBN:", isbnField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        // Shows a confirmation dialog with input fields for adding a book.
        if (option == JOptionPane.OK_OPTION) {
            // If the user clicks the "OK" button.
            String sql = "INSERT INTO books (title, author, isbn, quantity, price) VALUES (?, ?, ?, ?, ?)";
            // SQL query to insert a new book into the 'books' table.
            // This is a Create operation (C) in CRUD.
            PreparedStatement pstmt = null;
            try {
                pstmt = dbConnection.prepareStatement(sql); // Creates a prepared statement.
                pstmt.setString(1, titleField.getText()); // Sets the title parameter in the query.
                pstmt.setString(2, authorField.getText()); // Sets the author parameter.
                pstmt.setString(3, isbnField.getText()); // Sets the ISBN parameter.
                pstmt.setInt(4, Integer.parseInt(quantityField.getText())); // Sets the quantity parameter.
                pstmt.setDouble(5, Double.parseDouble(priceField.getText())); // Sets the price parameter.
                pstmt.executeUpdate(); // Executes the insert query.

                loadBooksFromDatabase(); // Reloads the book data from the database.
                updateTable(); // Updates the table to reflect the newly added book.
            } catch (SQLException | NumberFormatException ex) {
                // Handles SQL exceptions or if the quantity or price are not valid numbers.
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            } finally {
                closeResources(null, pstmt, null); // Closes the database resources.
            }
        }
    }

    public void editBook(Book book, Connection dbConnection) {
        // Allows the admin to edit an existing book's information.
        JTextField titleField = new JTextField(book.getTitle()); // Creates a text field pre-filled with the book's title.
        JTextField authorField = new JTextField(book.getAuthor()); // Creates a text field pre-filled with the author.
        JTextField isbnField = new JTextField(book.getIsbn()); // Creates a text field pre-filled with the ISBN.
        isbnField.setEditable(false); // Makes the ISBN field non-editable as it's usually the identifier.
        JTextField quantityField = new JTextField(String.valueOf(book.getQuantity())); // Creates a text field pre-filled with the quantity.
        JTextField priceField = new JTextField(String.valueOf(book.getPrice())); // Creates a text field pre-filled with the price.

        Object[] message = {
                // Creates an array of objects for the edit dialog.
                "Title:", titleField,
                "Author:", authorField,
                "ISBN:", isbnField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        // Shows a confirmation dialog with input fields for editing the book.
        if (option == JOptionPane.OK_OPTION) {
            // If the user clicks the "OK" button.
            String sql = "UPDATE books SET title=?, author=?, quantity=?, price=? WHERE isbn=?";
            // SQL query to update an existing book in the 'books' table based on ISBN.
            // This is an Update operation (U) in CRUD.
            PreparedStatement pstmt = null;
            try {
                pstmt = dbConnection.prepareStatement(sql); // Creates a prepared statement.
                pstmt.setString(1, titleField.getText()); // Sets the new title.
                pstmt.setString(2, authorField.getText()); // Sets the new author.
                pstmt.setInt(3, Integer.parseInt(quantityField.getText())); // Sets the new quantity.
                pstmt.setDouble(4, Double.parseDouble(priceField.getText())); // Sets the new price.
                pstmt.setString(5, book.getIsbn()); // Sets the ISBN in the WHERE clause to identify the book to update.
                pstmt.executeUpdate(); // Executes the update query.

                loadBooksFromDatabase(); // Reloads the book data.
                updateTable(); // Updates the table to reflect the changes.
            } catch (SQLException | NumberFormatException ex) {
                // Handles exceptions.
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            } finally {
                closeResources(null, pstmt, null); // Closes resources.
            }
        }
    }

    public void removeBook(Book book, Connection dbConnection) {
        // Allows the admin to remove a book from the database.
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        // Shows a confirmation dialog before deleting the book.

        if (confirm == JOptionPane.YES_OPTION) {
            // If the user confirms the deletion.
            String sql = "DELETE FROM books WHERE isbn = ?";
            // SQL query to delete a book from the 'books' table based on ISBN.
            // This is a Delete operation (D) in CRUD.
            PreparedStatement pstmt = null;
            try {
                pstmt = dbConnection.prepareStatement(sql); // Creates a prepared statement.
                pstmt.setString(1, book.getIsbn()); // Sets the ISBN in the WHERE clause to identify the book to delete.
                pstmt.executeUpdate(); // Executes the delete query.

                loadBooksFromDatabase(); // Reloads the book data.
                updateTable(); // Updates the table to reflect the removal.
            } catch (SQLException ex) {
                // Handles exceptions.
                JOptionPane.showMessageDialog(frame, "Error removing book: " + ex.getMessage());
            } finally {
                closeResources(null, pstmt, null); // Closes resources.
            }
        }
    }

    private void searchBooks(Connection dbConnection) {
        // Searches for books in the database based on the search term.
        String searchTerm = searchField.getText().toLowerCase(); // Gets the search term and converts it to lowercase.
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? OR LOWER(isbn) LIKE ?";
        // SQL query to select books whose title, author, or ISBN contains the search term (case-insensitive).
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = dbConnection.prepareStatement(sql); // Creates a prepared statement.
            String term = "%" + searchTerm + "%"; // Adds wildcard characters to the search term for "contains" logic.
            pstmt.setString(1, term); // Sets the search term for the title.
            pstmt.setString(2, term); // Sets the search term for the author.
            pstmt.setString(3, term); // Sets the search term for the ISBN.

            rs = pstmt.executeQuery(); // Executes the search query.
            books.clear(); // Clears the current list of books.
            while (rs.next()) {
                // Adds the matching books to the list.
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }
            updateTable(); // Updates the table with the search results.
        } catch (SQLException ex) {
            // Handles exceptions.
            JOptionPane.showMessageDialog(frame, "Search error: " + ex.getMessage());
        } finally {
            closeResources(rs, pstmt, null); // Closes resources.
        }
    }

    private void viewSales(Connection dbConnection) {
        // Retrieves and displays sales transaction data from the database.
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        // SQL query to select all transactions, ordered by date in descending order.
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = dbConnection.prepareStatement(sql); // Creates a prepared statement.
            rs = pstmt.executeQuery(); // Executes the query.

            StringBuilder salesData = new StringBuilder(); // Uses a StringBuilder for efficient string concatenation.
            salesData.append("Transaction History:\n\n");

            while (rs.next()) {
                // Appends each transaction's details to the salesData string.
                salesData.append("ID: ").append(rs.getString("transaction_id"))
                        .append("\nDate: ").append(rs.getTimestamp("transaction_date"))
                        .append("\nPhone: ").append(rs.getString("phone_number"))
                        .append("\nTotal: $").append(rs.getDouble("total_cost"))
                        .append("\nAddress: ").append(rs.getString("address"))
                        .append("\n\n");
            }

            JTextArea textArea = new JTextArea(salesData.toString()); // Creates a text area to display the sales data.
            textArea.setEditable(false); // Makes the text area read-only.
            JScrollPane scrollPane = new JScrollPane(textArea); // Creates a scroll pane for the text area.
            scrollPane.setPreferredSize(new Dimension(400, 300)); // Sets the preferred size of the scroll pane.

            JOptionPane.showMessageDialog(frame, scrollPane, "Sales Data",
                    JOptionPane.INFORMATION_MESSAGE); // Displays the sales data in a message dialog.
        } catch (SQLException ex) {
            // Handles exceptions.
            JOptionPane.showMessageDialog(frame, "Error reading sales data: " + ex.getMessage());
        } finally {
            closeResources(rs, pstmt, null); // Closes resources.
        }
    }

    private void updateTable() {
        // Updates the book table with the data from the 'books' list.
        tableModel.setRowCount(0); // Clears all existing rows from the table model.
        for (Book book : books) {
            // Adds a new row to the table for each book in the list.
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getQuantity(),
                    book.getPrice(),
                    "Edit", // "Edit" button label.
                    "Remove" // "Remove" button label.
            });
        }
    }

    private void closeConnection(Connection connection) {
        // This method is not actively used in the current implementation but shows how to close a connection.
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeResources(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        // Helper method to close database resources safely.
        try {
            if (resultSet != null) resultSet.close(); // Closes the ResultSet if it's not null.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) preparedStatement.close(); // Closes the PreparedStatement if it's not null.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // The connection is managed at the Admin class level and closed in the cleanup method.
        // Closing it here for each operation might be too aggressive and cause issues if operations are chained.
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        // Custom renderer for the button cells in the table.
        public ButtonRenderer() {
            setOpaque(true); // Makes the button paint all its pixels.
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus,
                                                     int row, int column) {
            // Returns the button component for rendering in the table cell.
            setText((value == null) ? "" : value.toString()); // Sets the button text.
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        // Custom editor for the button cells in the table, handles button clicks.
        protected JButton button;
        private String label;
        private boolean isPushed;
        private final String action; // Stores the action associated with the button ("Edit" or "Remove").
        private final Admin admin; // Reference to the Admin class to call editBook or removeBook methods.

        public ButtonEditor(JCheckBox checkBox, String action, Admin admin) {
            super(checkBox); // Calls the constructor of the superclass.
            this.action = action;
            this.admin = admin;
            button = new JButton(); // Creates a new JButton.
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped()); // Stops editing when the button is clicked.
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected, int row, int column) {
            // Returns the button component for editing in the table cell.
            label = (value == null) ? "" : value.toString(); // Gets the button label.
            button.setText(label); // Sets the button text.
            isPushed = true; // Indicates that the button is pushed.
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            // Called when editing is stopped, performs the action based on the button clicked.
            if (isPushed) {
                int selectedRow = bookTable.getSelectedRow(); // Gets the selected row index.
                if (selectedRow != -1 && selectedRow < books.size()) {
                    // Checks if a valid row is selected.
                    Book selectedBook = books.get(selectedRow); // Gets the Book object from the selected row.
                    if ("Edit".equals(action)) {
                        // If the "Edit" button was clicked, call the editBook method.
                        editBook(selectedBook, dbConnection);
                    } else if ("Remove".equals(action)) {
                        // If the "Remove" button was clicked, call the removeBook method.
                        removeBook(selectedBook, dbConnection);
                    }
                }
            }
            isPushed = false; // Reset the pushed state.
            return label; // Returns the label.
        }

        @Override
        public boolean stopCellEditing() {
            // Stops the editing process.
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public JFrame getFrame() {
        // Returns the main frame of the application.
        return frame;
    }
}
content_copy
download
Use code with caution.
Java
