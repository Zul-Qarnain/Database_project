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
    private Connection dbConnection;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Admin());
    }

    public Admin() {
        books = new ArrayList<>();
        try {
            dbConnection = DatabaseUtils.getConnection();
            initializeUI();
            loadBooksFromDatabase();
            updateTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        frame = new JFrame("Book Store Admin");
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
        btnSearch.addActionListener(e -> searchBooks());
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
                return column == 5 || column == 6;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        bookTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton btnViewSales = new JButton("View Sales");
        btnViewSales.addActionListener(e -> viewSales());
        frame.getContentPane().add(btnViewSales, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadBooksFromDatabase() throws SQLException {
        books.clear();
        String sql = "SELECT * FROM books ORDER BY title";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                ));
            }
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
            try {
                String sql = "INSERT INTO books (title, author, isbn, quantity, price) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
                    pstmt.setString(1, titleField.getText());
                    pstmt.setString(2, authorField.getText());
                    pstmt.setString(3, isbnField.getText());
                    pstmt.setInt(4, Integer.parseInt(quantityField.getText()));
                    pstmt.setDouble(5, Double.parseDouble(priceField.getText()));
                    pstmt.executeUpdate();

                    loadBooksFromDatabase();
                    updateTable();
                }
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        }
    }

    private void editBook(Book book) {
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
            try {
                String sql = "UPDATE books SET title=?, author=?, quantity=?, price=? WHERE isbn=?";
                try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
                    pstmt.setString(1, titleField.getText());
                    pstmt.setString(2, authorField.getText());
                    pstmt.setInt(3, Integer.parseInt(quantityField.getText()));
                    pstmt.setDouble(4, Double.parseDouble(priceField.getText()));
                    pstmt.setString(5, book.getIsbn());
                    pstmt.executeUpdate();

                    loadBooksFromDatabase();
                    updateTable();
                }
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        }
    }

    private void removeBook(Book book) {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to delete this book?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM books WHERE isbn = ?";
                try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
                    pstmt.setString(1, book.getIsbn());
                    pstmt.executeUpdate();

                    loadBooksFromDatabase();
                    updateTable();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error removing book: " + ex.getMessage());
            }
        }
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase();
        try {
            String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? OR LOWER(isbn) LIKE ?";
            try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
                String term = "%" + searchTerm + "%";
                pstmt.setString(1, term);
                pstmt.setString(2, term);
                pstmt.setString(3, term);

                ResultSet rs = pstmt.executeQuery();
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
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Search error: " + ex.getMessage());
        }
    }

    private void viewSales() {
        try {
            String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
            try (PreparedStatement pstmt = dbConnection.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

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
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading sales data: " + ex.getMessage());
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
        private String action;

        public ButtonEditor(JCheckBox checkBox, String action) {
            super(checkBox);
            this.action = action;
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
                if (selectedRow != -1) {
                    Book selectedBook = books.get(selectedRow);
                    if (action.equals("Edit")) {
                        editBook(selectedBook);
                    } else if (action.equals("Remove")) {
                        removeBook(selectedBook);
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
}