import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * ScoreManager - Manages saving and loading player scores.
 * 
 * Each player's scores are stored in a separate file named after their username.
 * Each score entry includes the score value, date, and time.
 */
public class ScoreManager {
    
    private static final String SCORES_DIRECTORY = "scores";
    private static final String FILE_EXTENSION = ".txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Static initializer to create the scores directory if it doesn't exist
     */
    static {
        File scoreDir = new File(SCORES_DIRECTORY);
        if (!scoreDir.exists()) {
            scoreDir.mkdir();
        }
    }
    
    /**
     * Save a player's score to their score file
     * 
     * @param username The player's username
     * @param score The score achieved
     */
    public static void saveScore(String username, int score) {
        try {
            File scoreFile = new File(SCORES_DIRECTORY + File.separator + username + FILE_EXTENSION);
            FileWriter fw = new FileWriter(scoreFile, true);
            
            // Format: score | date | time
            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String scoreEntry = score + " | " + timestamp + "\n";
            
            fw.write(scoreEntry);
            fw.close();
            
            System.out.println("Score saved for " + username + ": " + score);
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }
    
    /**
     * Load all scores for a specific player
     * 
     * @param username The player's username
     * @return ArrayList of score strings, or empty list if file doesn't exist
     */
    public static ArrayList<String> loadScores(String username) {
        ArrayList<String> scores = new ArrayList<>();
        
        try {
            File scoreFile = new File(SCORES_DIRECTORY + File.separator + username + FILE_EXTENSION);
            
            if (!scoreFile.exists()) {
                System.out.println("No score file found for user: " + username);
                return scores;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(scoreFile));
            String line;
            
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    scores.add(line.trim());
                }
            }
            br.close();
            
        } catch (IOException e) {
            System.out.println("Error loading scores: " + e.getMessage());
        }
        
        return scores;
    }
    
    /**
     * Get formatted score display for a player
     * Shows all scores with ranking
     * 
     * @param username The player's username
     * @return Formatted string of all scores, or message if no scores exist
     */
    public static String getFormattedScores(String username) {
        ArrayList<String> scores = loadScores(username);
        
        if (scores.isEmpty()) {
            return "No scores found for user: " + username;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Score History for ").append(username).append(" ===\n\n");
        
        // Parse scores and sort them in descending order
        ArrayList<ScoreEntry> entries = new ArrayList<>();
        for (String scoreStr : scores) {
            String[] parts = scoreStr.split(" \\| ");
            if (parts.length == 2) {
                try {
                    int scoreValue = Integer.parseInt(parts[0].trim());
                    String timestamp = parts[1].trim();
                    entries.add(new ScoreEntry(scoreValue, timestamp));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing score: " + scoreStr);
                }
            }
        }
        
        // Sort by score descending
        entries.sort((a, b) -> Integer.compare(b.score, a.score));
        
        // Display scores with ranking
        for (int i = 0; i < entries.size(); i++) {
            ScoreEntry entry = entries.get(i);
            sb.append(String.format("#%d: %d points - %s\n", 
                i + 1, entry.score, entry.timestamp));
        }
        
        // Show statistics
        if (!entries.isEmpty()) {
            int maxScore = entries.get(0).score;
            int minScore = entries.get(entries.size() - 1).score;
            double avgScore = entries.stream().mapToInt(e -> e.score).average().orElse(0);
            
            sb.append("\n--- Statistics ---\n");
            sb.append(String.format("Total Games: %d\n", entries.size()));
            sb.append(String.format("Highest Score: %d\n", maxScore));
            sb.append(String.format("Lowest Score: %d\n", minScore));
            sb.append(String.format("Average Score: %.1f\n", avgScore));
        }
        
        return sb.toString();
    }
    
    /**
     * Inner class to represent a score entry
     */
    private static class ScoreEntry {
        int score;
        String timestamp;
        
        ScoreEntry(int score, String timestamp) {
            this.score = score;
            this.timestamp = timestamp;
        }
    }
}
