import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Book> books;
    private Connection dbConnection; // Add connection as instance variable

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Admin());
    }

    public Admin() {
        books = new ArrayList<>();
        try {
            // Store connection as instance variable instead of local variable
            dbConnection = DatabaseUtils.getConnection();
            initializeUI();
            loadBooksFromDatabase();
            updateTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add this method to properly close connection when Admin window closes
    private void cleanup() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeUI() {
        frame = new JFrame("Book Store Admin");
        // Add window listener to cleanup when closing
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cleanup();
            }
        });
        frame.setBounds(100, 100, 900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.addActionListener(e -> addBook());
        buttonPanel.add(btnAddBook);

        JLabel lblSearch = new JLabel("Search:");
        searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks(dbConnection));
        buttonPanel.add(lblSearch);
        buttonPanel.add(searchField);
        buttonPanel.add(btnSearch);

        topPanel.add(buttonPanel, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            frame.dispose();
            Welcome.main(new String[]{});
        });
        topPanel.add(btnLogout, BorderLayout.EAST);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "ISBN", "Quantity", "Price", "Edit", "Remove"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Only Edit and Remove columns are editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class; // All columns contain String data
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit", this));
        bookTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove", this));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton btnViewSales = new JButton("View Sales");
        btnViewSales.addActionListener(e -> viewSales(dbConnection));
        frame.getContentPane().add(btnViewSales, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadBooksFromDatabase() {
        books.clear();
        String sql = "SELECT * FROM books ORDER BY title";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = dbConnection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "ISBN:", isbnField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO books (title, author, isbn, quantity, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = null;
            try {
                pstmt = dbConnection.prepareStatement(sql);
                pstmt.setString(1, titleField.getText());
                pstmt.setString(2, authorField.getText());
                pstmt.setString(3, isbnField.getText());
                pstmt.setInt(4, Integer.parseInt(quantityField.getText()));
                pstmt.setDouble(5, Double.parseDouble(priceField.getText()));
                pstmt.executeUpdate();

                loadBooksFromDatabase();
                updateTable();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            } finally {
                closeResources(null, pstmt, null);
            }
        }
    }

    public void editBook(Book book, Connection dbConnection) {
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField isbnField = new JTextField(book.getIsbn());
        isbnField.setEditable(false);
        JTextField quantityField = new JTextField(String.valueOf(book.getQuantity()));
        JTextField priceField = new JTextField(String.valueOf(book.getPrice()));

        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "ISBN:", isbnField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String sql = "UPDATE books SET title=?, author=?, quantity=?, price=? WHERE isbn=?";
            PreparedStatement pstmt = null;
            try {
                pstmt = dbConnection.prepareStatement(sql);
                pstmt.setString(1, titleField.getText());
                pstmt.setString(2, authorField.getText());
                pstmt.setInt(3, Integer.parseInt(quantityField.getText()));
                pstmt.setDouble(4, Double.parseDouble(priceField.getText()));
                pstmt.setString(5, book.getIsbn());
                pstmt.executeUpdate();

                loadBooksFromDatabase();
                updateTable();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            } finally {
                closeResources(null, pstmt, null);
            }
        }
    }

    public void removeBook(Book book, Connection dbConnection) {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM books WHERE isbn = ?";
            PreparedStatement pstmt = null;
            try {
                pstmt = dbConnection.prepareStatement(sql);
                pstmt.setString(1, book.getIsbn());
                pstmt.executeUpdate();

                loadBooksFromDatabase();
                updateTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error removing book: " + ex.getMessage());
            } finally {
                closeResources(null, pstmt, null);
            }
        }
    }

    private void searchBooks(Connection dbConnection) {
        String searchTerm = searchField.getText().toLowerCase();
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? OR LOWER(isbn) LIKE ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = dbConnection.prepareStatement(sql);
            String term = "%" + searchTerm + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);
            pstmt.setString(3, term);

            rs = pstmt.executeQuery();
            books.clear();
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }
            updateTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Search error: " + ex.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
    }

    private void viewSales(Connection dbConnection) {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = dbConnection.prepareStatement(sql);
            rs = pstmt.executeQuery();

            StringBuilder salesData = new StringBuilder();
            salesData.append("Transaction History:\n\n");

            while (rs.next()) {
                salesData.append("ID: ").append(rs.getString("transaction_id"))
                        .append("\nDate: ").append(rs.getTimestamp("transaction_date"))
                        .append("\nPhone: ").append(rs.getString("phone_number"))
                        .append("\nTotal: $").append(rs.getDouble("total_cost"))
                        .append("\nAddress: ").append(rs.getString("address"))
                        .append("\n\n");
            }

            JTextArea textArea = new JTextArea(salesData.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(frame, scrollPane, "Sales Data",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading sales data: " + ex.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
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
                    "Edit",
                    "Remove"
            });
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeResources(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // We are now managing connection per operation, so close it here as well if passed.
        // If the connection was obtained within the method, it should be passed here to be closed.
        // In the current structure, the connection is passed to the constructor and managed there.
        // If you want to close the connection after each DB operation, you would need to modify
        // how the connection is obtained and passed around.
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus,
                                                     int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private final String action;
        private final Admin admin;

        public ButtonEditor(JCheckBox checkBox, String action, Admin admin) {
            super(checkBox);
            this.action = action;
            this.admin = admin;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1 && selectedRow < books.size()) {
                    Book selectedBook = books.get(selectedRow);
                    if ("Edit".equals(action)) {
                        editBook(selectedBook, dbConnection);
                    } else if ("Remove".equals(action)) {
                        removeBook(selectedBook, dbConnection);
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public JFrame getFrame() {
        return frame;
    }
}

