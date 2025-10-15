package BankingApp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BankingApp {
    private BankingService bankingService;
    private Scanner scanner;
    private boolean running;
    
    public BankingApp() {
        this.bankingService = new BankingService();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        System.out.println("=== Welcome to Secure Banking System ===");
        
        while (running) {
            if (!bankingService.isUserLoggedIn()) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
        
        scanner.close();
        System.out.println("Thank you for using Secure Banking System!");
    }
    
    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleRegistration();
                break;
            case "3":
                running = false;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private void showUserMenu() {
        System.out.println("\n=== User Menu ===");
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. View Transactions");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                handleCreateAccount();
                break;
            case "2":
                handleViewAccounts();
                break;
            case "3":
                handleDeposit();
                break;
            case "4":
                handleWithdraw();
                break;
            case "5":
                handleViewTransactions();
                break;
            case "6":
                bankingService.logout();
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private void handleLogin() {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            if (bankingService.login(username, password)) {
                System.out.println("Login successful! Welcome, " + bankingService.getCurrentUser().getFirstName());
            }
        } catch (BankingException e) {
            System.out.println("Login failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during login. Please try again.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    private void handleRegistration() {
        try {
            System.out.print("Username: ");
            String username = DataValidator.sanitizeInput(scanner.nextLine());
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("First Name: ");
            String firstName = DataValidator.sanitizeInput(scanner.nextLine());
            System.out.print("Last Name: ");
            String lastName = DataValidator.sanitizeInput(scanner.nextLine());
            System.out.print("Email: ");
            String email = DataValidator.sanitizeInput(scanner.nextLine());
            
            if (bankingService.registerUser(username, password, firstName, lastName, email)) {
                System.out.println("Registration successful! You can now login.");
            }
        } catch (BankingException e) {
            System.out.println("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during registration. Please try again.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    private void handleCreateAccount() {
        try {
            System.out.println("Select Account Type:");
            System.out.println("1. Savings");
            System.out.println("2. Checking");
            System.out.println("3. Current");
            System.out.print("Choose account type: ");
            
            String choice = scanner.nextLine();
            BankAccount.AccountType accountType;
            
            switch (choice) {
                case "1":
                    accountType = BankAccount.AccountType.SAVINGS;
                    break;
                case "2":
                    accountType = BankAccount.AccountType.CHECKING;
                    break;
                case "3":
                    accountType = BankAccount.AccountType.CURRENT;
                    break;
                default:
                    System.out.println("Invalid account type.");
                    return;
            }
            
            BankAccount account = bankingService.createAccount(accountType);
            System.out.println("Account created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType());
            
        } catch (BankingException e) {
            System.out.println("Account creation failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred. Please try again.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    private void handleViewAccounts() {
        try {
            List<BankAccount> accounts = bankingService.getUserAccounts();
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }
            
            System.out.println("\n=== Your Accounts ===");
            for (BankAccount account : accounts) {
                System.out.printf("Account: %s | Type: %s | Balance: $%.2f%n",
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getBalance());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching accounts.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    private void handleDeposit() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            System.out.print("Enter amount to deposit: ");
            String amountStr = scanner.nextLine();
            System.out.print("Enter description: ");
            String description = DataValidator.sanitizeInput(scanner.nextLine());
            
            if (!DataValidator.isValidAmount(amountStr)) {
                System.out.println("Invalid amount format.");
                return;
            }
            
            BigDecimal amount = new BigDecimal(amountStr);
            bankingService.deposit(accountNumber, amount, description);
            System.out.println("Deposit successful!");
            
        } catch (BankingException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format.");
        } catch (Exception e) {
            System.out.println("An error occurred during deposit.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    private void handleWithdraw() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            System.out.print("Enter amount to withdraw: ");
            String amountStr = scanner.nextLine();
            System.out.print("Enter description: ");
            String description = DataValidator.sanitizeInput(scanner.nextLine());
            
            if (!DataValidator.isValidAmount(amountStr)) {
                System.out.println("Invalid amount format.");
                return;
            }
            
            BigDecimal amount = new BigDecimal(amountStr);
            bankingService.withdraw(accountNumber, amount, description);
            System.out.println("Withdrawal successful!");
            
        } catch (BankingException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format.");
        } catch (Exception e) {
            System.out.println("An error occurred during withdrawal.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    private void handleViewTransactions() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            
            List<Transaction> transactions = bankingService.getAccountTransactions(accountNumber);
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }
            
            System.out.println("\n=== Transaction History ===");
            for (Transaction transaction : transactions) {
                System.out.printf("%s | %s | Amount: $%.2f | Balance: $%.2f | %s%n",
                        transaction.getTimestamp(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getDescription());
            }
        } catch (BankingException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while fetching transactions.");
            System.err.println("Technical error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        BankingApp app = new BankingApp();
        app.start();
    }
}