import java.sql.*;

public class DatabaseHelper {
    private Connection connection;
    
    public DatabaseHelper() {
        connect();
    }
    
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/authdb";
            String user = "root";
            String password = "";
            
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found!");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Connection failed! Creating local SQLite database instead...");
            // Fallback to SQLite
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:auth.db");
                System.out.println("SQLite database created!");
            } catch (Exception ex) {
                System.out.println("SQLite also failed: " + ex.getMessage());
            }
        }
    }
    
    public void createUsersTable() {
        String mysqlSql = "CREATE TABLE IF NOT EXISTS users (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                         "username VARCHAR(50) UNIQUE NOT NULL," +
                         "password_hash VARCHAR(255) NOT NULL," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                         ")";
        
        String sqliteSql = "CREATE TABLE IF NOT EXISTS users (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "username TEXT UNIQUE NOT NULL," +
                          "password_hash TEXT NOT NULL," +
                          "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                          ")";
        
        try {
            Statement stmt = connection.createStatement();
            
            // Try MySQL syntax first
            try {
                stmt.execute(mysqlSql);
            } catch (SQLException e) {
                // If MySQL fails, try SQLite syntax
                stmt.execute(sqliteSql);
            }
            
            stmt.close();
            System.out.println("Users table ready!");
            
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            pstmt.close();
            return exists;
            
        } catch (SQLException e) {
            System.out.println("Error checking user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean registerUser(String username, String passwordHash) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            return rows > 0;
            
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean authenticateUser(String username, String passwordHash) {
        String sql = "SELECT username FROM users WHERE username = ? AND password_hash = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            
            ResultSet rs = pstmt.executeQuery();
            boolean authenticated = rs.next();
            
            rs.close();
            pstmt.close();
            return authenticated;
            
        } catch (SQLException e) {
            System.out.println("Error authenticating: " + e.getMessage());
            return false;
        }
    }
    
    public void getUserInfo(String username) {
        String sql = "SELECT username, created_at FROM users WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("\n=== Profile ===");
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Joined: " + rs.getString("created_at"));
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error getting user info: " + e.getMessage());
        }
    }
    
    public boolean updatePassword(String username, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setString(2, username);
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            return rows > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
    
    public void showAllUsers() {
        String sql = "SELECT id, username, created_at FROM users ORDER BY id";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== All Users ===");
            System.out.println("ID\tUsername\tJoined");
            System.out.println("--------------------------------");
            
            while (rs.next()) {
                System.out.printf("%d\t%-10s\t%s\n",
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("created_at").substring(0, 10)
                );
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error showing users: " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
