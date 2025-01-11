import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login {

    public static void main(String[] args) {
        // Create main frame for Login screen
        JFrame frame = new JFrame("Admin Login");
        // Terminate the program upon closing the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set dimensions of the frame
        frame.setSize(800, 600);
        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Create a JPanel for components, using GridBagLayout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // "Welcome back" label
        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, welcomeLabel, 0, 0, 2, 1, GridBagConstraints.CENTER); // Helper method for adding components

        // "Sign in" message label
        JLabel subLabel = new JLabel("Please sign in to access your account");
        subLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, subLabel, 0, 1, 2, 1, GridBagConstraints.CENTER);

        // "Username" label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, usernameLabel, 0, 2, 1, 1, GridBagConstraints.WEST);

        // Username input field
        JTextField usernameField = new JTextField(20);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username or email");
        addComponent(panel, usernameField, 0, 3, 2, 1, GridBagConstraints.WEST);

        // "Password" label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, passwordLabel, 0, 4, 1, 1, GridBagConstraints.WEST);

        // Password input field
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.putClientProperty("JPasswordField.placeholderText", "Enter your password");
        addComponent(panel, passwordField, 0, 5, 2, 1, GridBagConstraints.WEST);

        // "Remember me" checkbox
        JCheckBox rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        rememberMeCheckbox.setOpaque(false);
        addComponent(panel, rememberMeCheckbox, 0, 6, 1, 1, GridBagConstraints.WEST);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Authenticate admin credentials
                if (authenticateAdmin(username, password)) {
                    // Successful login
                    JOptionPane.showMessageDialog(frame, "Login successful!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose(); // Close login window
                    Admin.main(new String[]{}); // Open admin dashboard
                } else {
                    // Invalid credentials
                    JOptionPane.showMessageDialog(frame, "Invalid username or password!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addComponent(panel, loginButton, 0, 7, 2, 1, GridBagConstraints.CENTER);

        // "Don't have an account?" label
        JLabel signupLabel = new JLabel("Don't have an account? ");
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, signupLabel, 0, 8, 1, 1, GridBagConstraints.EAST);

        // "Sign up" link
        JLabel signupLink = new JLabel("<html><a href='#'>Sign up</a></html>");
        signupLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        signupLink.setForeground(Color.BLUE); // Blue link color
        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose(); // Close login window
                SignUp.main(new String[]{}); // Open signup window
            }
        });
        addComponent(panel, signupLink, 1, 8, 1, 1, GridBagConstraints.WEST);

        // Add the panel to the frame and set the frame visible
        frame.add(panel);
        frame.setVisible(true);
    }

    // Helper function for adding components to the panel using GridBagConstraints
    private static void addComponent(JPanel panel, JComponent component, int gridx, int gridy,
                                     int gridwidth, int gridheight, int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 20, 5, 20);
        constraints.anchor = anchor;
        panel.add(component, constraints);
    }

    // Admin authentication method (IMPORTANT: Insecure, for demo only!)
    private static boolean authenticateAdmin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("admininfo.txt"))) {
            String line;
            // Read each line of the admininfo.txt file
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                // Check if the username and password match any stored credentials
                if (credentials.length == 2 &&
                        credentials[0].equals(username) &&
                        credentials[1].equals(password)) {
                    return true; // Authentication successful
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading admin file: " + e.getMessage());
        }
        return false; // Authentication failed
    }
}
