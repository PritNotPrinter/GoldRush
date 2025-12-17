import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

/*
 * LoginPage - Authentication interface for the Coin Collector Game.
 */

public class LoginPage extends JPanel {
    
    private JTextField userField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);
    private JLabel status = new JLabel("Login or Register");
    private ArrayList<String> userID = new ArrayList<>();
    private ArrayList<String> userPass = new ArrayList<>();
    private boolean loginSuccessful = false;
    private String loggedInUser = "";
    
    /**
     * Interface for callback when login is successful
     */
    public interface LoginListener {
        void onLoginSuccess();
    }
    
    private LoginListener loginListener;
    
    public LoginPage() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 210, 240));
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title label with better styling
        JLabel titleLabel = new JLabel("Gold Rush");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        
        // Create a semi-transparent panel for form elements
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 230));
        formPanel.setBorder(new RoundedBorder(15, new Color(100, 150, 255), 2));
        
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 15, 10, 15);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username label
        formGbc.gridwidth = 1;
        formGbc.gridy = 0;
        formGbc.gridx = 0;
        formGbc.anchor = GridBagConstraints.WEST;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(userLabel, formGbc);
        
        // Username field
        formGbc.gridx = 1;
        userField.setFont(new Font("Arial", Font.PLAIN, 12));
        userField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(userField, formGbc);
        
        // Password label
        formGbc.gridy = 1;
        formGbc.gridx = 0;
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.fill = GridBagConstraints.NONE;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passLabel, formGbc);
        
        // Password field
        formGbc.gridx = 1;
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        passField.setFont(new Font("Arial", Font.PLAIN, 12));
        passField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(passField, formGbc);
        
        // Add form panel to main
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 50, 20, 50);
        add(formPanel, gbc);
        
        // Button panel with better styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));  // Transparent
        
        JButton login = new JButton("Log in");
        JButton register = new JButton("Register");
        JButton viewScores = new JButton("View Scores");
        
        // Style buttons
        Dimension buttonSize = new Dimension(120, 35);
        for (JButton btn : new JButton[]{login, register, viewScores}) {
            btn.setPreferredSize(buttonSize);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(100, 150, 255));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        
        login.addActionListener(e -> loginUser());
        register.addActionListener(e -> registerUser());
        viewScores.addActionListener(e -> showScoresDialog());
        
        buttonPanel.add(login);
        buttonPanel.add(register);
        buttonPanel.add(viewScores);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(buttonPanel, gbc);
        
        // Status label
        status.setForeground(Color.BLUE);
        status.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(status, gbc);
        
        // Load existing user data
        loadUserData();
    }
    

    
    /**
     * Set a listener to be notified when login is successful
     */
    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
    
    /**
     * Load user credentials from text files
     */
    private void loadUserData() {
        try {
            File usernameFile = new File("usernames.txt");
            File passwordFile = new File("passwords.txt");
            
            if (usernameFile.exists()) {
                BufferedReader userReader = new BufferedReader(new FileReader(usernameFile));
                String line;
                while ((line = userReader.readLine()) != null) {
                    userID.add(line);
                }
                userReader.close();
            }
            
            if (passwordFile.exists()) {
                BufferedReader passReader = new BufferedReader(new FileReader(passwordFile));
                String line;
                while ((line = passReader.readLine()) != null) {
                    userPass.add(line);
                }
                passReader.close();
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }
    
    /**
     * Authenticate user login
     */
    private void loginUser() {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        
        if ((username != null && !username.trim().isEmpty()) && (password != null && !password.trim().isEmpty())) {
            // Check if username exists
            int userIndex = userID.indexOf(username);
            if (userIndex != -1 && userPass.get(userIndex).equals(password)) {
                status.setText("Login successful");
                status.setForeground(new Color(0, 150, 0));
                loginSuccessful = true;
                loggedInUser = username;
                
                // Notify listener and transition to game
                if (loginListener != null) {
                    SwingUtilities.invokeLater(() -> loginListener.onLoginSuccess());
                }
            } else {
                status.setText("Invalid credentials");
                status.setForeground(new Color(200, 0, 0));
                loginSuccessful = false;
            }
        } else {
            status.setText("Please fill in all fields");
            status.setForeground(new Color(200, 0, 0));
        }
    }
    
    /**
     * Register a new user
     */
    private void registerUser() {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        
        if ((username != null && !username.trim().isEmpty()) && (password != null && !password.trim().isEmpty())) {
            // Check if username already exists
            if (userID.contains(username)) {
                status.setText("Username already exists");
                status.setForeground(new Color(200, 0, 0));
                return;
            }
            
            try {
                // Append to files instead of overwriting
                FileWriter usernameOut = new FileWriter("usernames.txt", true);
                FileWriter passwordOut = new FileWriter("passwords.txt", true);
                
                usernameOut.append(username).append("\n");
                passwordOut.append(password).append("\n");
                usernameOut.close();
                passwordOut.close();
                
                // Add to ArrayLists
                userID.add(username);
                userPass.add(password);
                
                status.setText("Registration successful");
                status.setForeground(new Color(0, 150, 0));
                userField.setText("");
                passField.setText("");
                
                System.out.println("User registered: " + username);
            } catch (IOException e) {
                status.setText("Error saving user data");
                status.setForeground(new Color(200, 0, 0));
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            status.setText("Please fill in all fields");
            status.setForeground(new Color(200, 0, 0));
        }
    }
    
    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
    
    /**
     * Get the logged-in username
     */
    public String getLoggedInUser() {
        return loggedInUser;
    }
    
    /**
     * Show scores for a specific user
     */
    private void showScoresDialog() {
        JFrame scoresFrame = new JFrame("View Player Scores");
        scoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        scoresFrame.setSize(500, 600);
        scoresFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JLabel searchLabel = new JLabel("Enter username:");
        JTextField usernameField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(usernameField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Text area for displaying scores
        JTextArea scoresArea = new JTextArea();
        scoresArea.setEditable(false);
        scoresArea.setLineWrap(true);
        scoresArea.setWrapStyleWord(true);
        scoresArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(scoresArea);
        
        // Search button action
        searchButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                scoresArea.setText("Please enter a username.");
            } else {
                String scores = ScoreManager.getFormattedScores(username);
                scoresArea.setText(scores);
                scoresArea.setCaretPosition(0);
            }
        });
        
        // Allow Enter key to search
        usernameField.addActionListener(e -> searchButton.doClick());
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        scoresFrame.setContentPane(panel);
        scoresFrame.setVisible(true);
    }
    
    /**
     * Main method for testing the LoginPage
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Coin Collector - Login");
        LoginPage loginPage = new LoginPage();
        frame.setContentPane(loginPage);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
