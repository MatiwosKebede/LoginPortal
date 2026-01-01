import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseHelper db = new DatabaseHelper();
        AuthManager auth = new AuthManager();
        
        System.out.println("=== SIMPLE LOGIN SYSTEM ===");
        
        // Create users table if not exists
        db.createUsersTable();
        
        boolean running = true;
        while (running) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Show Users");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String regUser = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String regPass = scanner.nextLine();
                    auth.register(regUser, regPass);
                    break;
                    
                case 2:
                    System.out.print("Enter username: ");
                    String loginUser = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPass = scanner.nextLine();
                    
                    if (auth.login(loginUser, loginPass)) {
                        System.out.println("\n✓ Login successful!");
                        System.out.println("Welcome, " + loginUser + "!");
                        
                        // User menu after login
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n--- User Menu ---");
                            System.out.println("1. View Profile");
                            System.out.println("2. Change Password");
                            System.out.println("3. Logout");
                            System.out.print("Choose: ");
                            
                            int userChoice = scanner.nextInt();
                            scanner.nextLine();
                            
                            switch (userChoice) {
                                case 1:
                                    auth.showProfile(loginUser);
                                    break;
                                case 2:
                                    System.out.print("Enter new password: ");
                                    String newPass = scanner.nextLine();
                                    auth.changePassword(loginUser, newPass);
                                    break;
                                case 3:
                                    System.out.println("Logging out...");
                                    loggedIn = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice!");
                            }
                        }
                    } else {
                        System.out.println("\n✗ Login failed!");
                    }
                    break;
                    
                case 3:
                    db.showAllUsers();
                    break;
                    
                case 4:
                    running = false;
                    db.closeConnection();
                    System.out.println("Goodbye!");
                    break;
                    
                default:
                    System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }
}
