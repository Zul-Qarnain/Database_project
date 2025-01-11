import javax.swing.*;
// ... other imports
import java.sql.*; // Add for JDBC


public class Payment extends JFrame {
    // ... existing code ...

    private Connection dbConnection; // Added database connection
    // ... other variables

    public Payment(List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
        initializePaymentGUI();
    }


    private void initializePaymentGUI() {
        // ... (GUI initialization code, mostly the same)

        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // ... (Get user inputs as before)


                try {

                    connectToDatabase(); // Get connection in this method
                    dbConnection.setAutoCommit(false);  // Start a transaction!



                    saveTransactionToFile(TRANSACTION_DATA_FILE, phoneNumber, transactionId, totalCost, address);


                    reduceBookQuantities(BOOK_DATA_FILE);  // Database update
                    showPurchasedBooks();

                    // ... other actions after successful purchase

                    dbConnection.commit();  // Commit the transaction!
                    clearCart();

                } catch (SQLException ex) {


                    try {
                        if (dbConnection != null) {
                            dbConnection.rollback(); // VERY IMPORTANT: Rollback on error to keep data consistent.
                        }
                    } catch (SQLException rollbackEx) {
                       // Log rollback failure.
                        rollbackEx.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();

                } finally { // Very important: Restore auto-commit
                    if (dbConnection != null) {
                        try {
                            dbConnection.setAutoCommit(true);

                        } catch (SQLException finalEx) {
                            finalEx.printStackTrace();

                        }

                    }
                }

                frame.dispose();
            }
        });


        cancelButton.addActionListener(e -> frame.dispose());


    }




    private void connectToDatabase() throws SQLException {
       // ... (Same connection code as in Admin.java or use a utility class)
    }


    private void reduceBookQuantities(String filename) throws SQLException { // Changed from filename to DB interaction
        // Using PreparedStatement now, much safer
        String updateSql = "UPDATE books SET quantity = quantity - ? WHERE isbn = ?"; // SQL with parameters
        try (PreparedStatement updateStmt = dbConnection.prepareStatement(updateSql)) {

            for (CartItem item : cartItems) {
                updateStmt.setInt(1, item.getQuantity());
                updateStmt.setString(2, item.getBook().getIsbn()); // Use ISBN
                updateStmt.executeUpdate();
            }
        }
    }




    // ... (Other methods remain mostly the same)


}
