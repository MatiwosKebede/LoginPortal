import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthManager {
    private DatabaseHelper db = new DatabaseHelper();
    
    public void register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty!");
            return;
        }
        
        if (db.userExists(username)) {
            System.out.println("Username already exists!");
            return;
        }
        
        String hashedPassword = hashPassword(password);
        if (db.registerUser(username, hashedPassword)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed!");
        }
    }
    
    public boolean login(String username, String password) {
        String hashedPassword = hashPassword(password);
        return db.authenticateUser(username, hashedPassword);
    }
    
    public void showProfile(String username) {
        db.getUserInfo(username);
    }
    
    public void changePassword(String username, String newPassword) {
        String hashedPassword = hashPassword(newPassword);
        if (db.updatePassword(username, hashedPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password!");
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e.getMessage());
            return password; // fallback (not secure for production)
        }
    }
}
