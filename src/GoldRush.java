import javax.swing.*;

/**
 * CoinCollectorGame - Main entry point for the Collect-the-Coins game.
 * 
 * This class initializes and launches the game window. It creates a JFrame
 * and displays a login/registration page before allowing access to the game.
 * After successful authentication, the GamePanel is shown.
 * 
 * Game Overview:
 * The player has 60 seconds and 3 lives to collect as many coins as possible.
 * A shape (the collection purse) follows the player's mouse cursor. When the
 * mouse is clicked, nearby coins are collected and the player gains points.
 * If a bomb is clicked, the player loses points and a life.
 * 
 * Coin Types and Values:
 * - Gold Coin: 10 points (15% spawn rate)
 * - Silver Coin: 5 points (25% spawn rate)
 * - Bronze Coin: 2 points (60% spawn rate)
 * 
 * Bombs:
 * - Penalty: -25 points and -1 life
 * - Visual feedback: Explosion animation
 * 
 * Controls:
 * - Move Mouse: Control the collection purse
 * - Click Mouse: Collect coins or detonate bombs
 * - Start Game: Begin a new game
 * - Pause/Resume: Pause the game during play
 * - Replay: Play another round after game ends
 */
public class GoldRush{
    
    private static JFrame frame;
    
    /**
     * Main method - Entry point for the application.
     * Creates and displays the login window first.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Run the game on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            // Create the main game window
            frame = new JFrame("Gold Rush - Login :]");
            
            // Set close operation
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Create and add the login panel
            LoginPage loginPage = new LoginPage();
            frame.setContentPane(loginPage);
            
            // Set up login listener to transition to game
            loginPage.setLoginListener(() -> transitionToGame(loginPage.getLoggedInUser()));
            
            // Pack the frame to fit the preferred size of its components
            frame.pack();
            
            // Center the window on the screen
            frame.setLocationRelativeTo(null);
            
            // Display the login window
            frame.setVisible(true);
        });
    }
    
    /**
     * Transition from login page to the main game
     * 
     * @param username The logged-in username
     */
    private static void transitionToGame(String username) {
        // Update frame title
        frame.setTitle("Gold Rush - " + username);
        
        // Create the game panel
        GamePanel gamePanel = new GamePanel();
        gamePanel.setUsername(username);
        
        // Set up logout listener to return to login page
        gamePanel.setLogoutListener(() -> transitionToLogin());
        
        // Replace content pane
        frame.setContentPane(gamePanel);
        
        // Refresh the frame
        frame.revalidate();
        frame.repaint();
        
        // Request focus for mouse and key events
        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Transition from game back to the login page
     */
    private static void transitionToLogin() {
        // Update frame title
        frame.setTitle("Gold Rush  - Login");
        
        // Create a new login page
        LoginPage loginPage = new LoginPage();
        
        // Set up login listener to transition to game
        loginPage.setLoginListener(() -> transitionToGame(loginPage.getLoggedInUser()));
        
        // Replace content pane
        frame.setContentPane(loginPage);
        
        // Refresh the frame
        frame.revalidate();
        frame.repaint();
        
        // Request focus
        loginPage.requestFocusInWindow();
    }
}
