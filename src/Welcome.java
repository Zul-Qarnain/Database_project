import javax.swing.*;
import java.awt.*;

public class Welcome {

    public static void main(String[] args) {
        // Create the main frame for the welcome screen
        JFrame frame = new JFrame("Library-Mangement.");
        // Terminate the application when the frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set the size of the frame
        frame.setSize(800, 600);
        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Create a JPanel to hold components and use GridBagLayout for layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // Create and add the main title label
        JLabel titleLabel = new JLabel("Welcome To");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, titleLabel, 0, 0, 1, 1, GridBagConstraints.CENTER); 
        // addComponent is a helper method to simplify adding components to the panel

        // Create and add the subtitle label
        JLabel subtitleLabel = new JLabel("Bokamari.");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, subtitleLabel, 0, 1, 1, 1, GridBagConstraints.CENTER);

        // Create and add the "Buy Book" button
        JButton buybook = new JButton("Buy Book");
        buybook.setFont(new Font("Arial", Font.BOLD, 16));
        // Add action listener for the "Buy Book" button
        buybook.addActionListener(e -> {
            // Dispose the current frame (welcome screen)
            frame.dispose();
            // Launch the BookStoreApp
            BookStoreApp.main(new String[]{});
        });
        addComponent(panel, buybook, 0, 2, 1, 1, GridBagConstraints.CENTER);

        // Create and add the "Admin Panel" button
        JButton loginButton = new JButton("AdminPanel");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        // Add action listener for the "Admin Panel" button
        loginButton.addActionListener(e -> {
            // Dispose the current frame (welcome screen)
            frame.dispose();
            // Launch the Admin login screen
            Login.main(new String[]{});
        });
        addComponent(panel, loginButton, 0, 3, 1, 1, GridBagConstraints.CENTER);

        // Add terms and conditions label
        JLabel termsLabel = new JLabel("By signing up, you agree to our ");
        termsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        addComponent(panel, termsLabel, 0, 4, 1, 1, GridBagConstraints.CENTER);

        // Create and add clickable terms and privacy links
        JLabel termsLink = createLinkLabel("<html><a href='#'>Terms</a> & <a href='#'>Privacy</a></html>");
        addComponent(panel, termsLink, 0, 5, 1, 1, GridBagConstraints.CENTER);

        // Add the panel to the frame and set the frame to visible
        frame.add(panel);
        frame.setVisible(true);
    }

    // Helper method to add components to the panel with GridBagConstraints
    private static void addComponent(JPanel panel, JComponent component, int gridx, int gridy,
                                     int gridwidth, int gridheight, int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 20, 10, 20);
        constraints.anchor = anchor;
        panel.add(component, constraints);
    }

    // Helper method to create clickable links
    private static JLabel createLinkLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(Color.BLUE);
        // Add a mouse listener to handle clicks
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Placeholder: Handle terms/privacy link clicks here
                System.out.println("Terms or Privacy link clicked");
            }
        });
        return label;
    }
}
