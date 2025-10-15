package BankingApp;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class BankingService {
    private Map<String, User> users;
    private Map<String, BankAccount> accounts;
    private List<Transaction> transactions;
    private User currentUser;
    
    public BankingService() {
        loadData();
    }
    
    private void loadData() {
        this.users = FileDataManager.loadUsers().stream()
                .collect(Collectors.toMap(User::getUsername, user -> user));
        this.accounts = FileDataManager.loadAccounts().stream()
                .collect(Collectors.toMap(BankAccount::getAccountNumber, account -> account));
        this.transactions = FileDataManager.loadTransactions();
    }
    
    private void saveData() {
        FileDataManager.saveUsers(new ArrayList<>(users.values()));
        FileDataManager.saveAccounts(new ArrayList<>(accounts.values()));
        FileDataManager.saveTransactions(transactions);
    }
    
    public boolean registerUser(String username, String password, String firstName, 
                               String lastName, String email) throws BankingException {
        
        if (!DataValidator.isValidUsername(username)) {
            throw new BankingException("Invalid username format");
        }
        if (!DataValidator.isValidName(firstName) || !DataValidator.isValidName(lastName)) {
            throw new BankingException("Invalid name format");
        }
        if (!DataValidator.isValidEmail(email)) {
            throw new BankingException("Invalid email format");
        }
        if (password == null || password.length() < 6) {
            throw new BankingException("Password must be at least 6 characters long");
        }
        
        if (users.containsKey(username)) {
            throw new BankingException("Username already exists");
        }
        
        String salt = PasswordHasher.generateSalt();
        String passwordHash = PasswordHasher.hashPassword(password, salt);
        
        User user = new User(username, passwordHash, salt, firstName, lastName, email);
        users.put(username, user);
        saveData();
        
        return true;
    }
    
    public boolean login(String username, String password) throws BankingException {
        User user = users.get(username);
        if (user == null || !user.isActive()) {
            throw new BankingException("Invalid username or password");
        }
        
        if (!PasswordHasher.verifyPassword(password, user.getSalt(), user.getPasswordHash())) {
            throw new BankingException("Invalid username or password");
        }
        
        currentUser = user;
        return true;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public BankAccount createAccount(BankAccount.AccountType accountType) throws BankingException {
        if (currentUser == null) {
            throw new BankingException("User not logged in");
        }
        
        String accountNumber = generateAccountNumber();
        BankAccount account = new BankAccount(accountNumber, currentUser.getUsername(), accountType);
        accounts.put(accountNumber, account);
        saveData();
        
        return account;
    }
    
    public BigDecimal getAccountBalance(String accountNumber) throws BankingException {
        BankAccount account = getAccountForCurrentUser(accountNumber);
        return account.getBalance();
    }
    
    public void deposit(String accountNumber, BigDecimal amount, String description) throws BankingException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Deposit amount must be positive");
        }
        
        BankAccount account = getAccountForCurrentUser(accountNumber);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, accountNumber, 
                Transaction.TransactionType.DEPOSIT, amount, newBalance, description);
        transactions.add(transaction);
        
        saveData();
    }
    
    public void withdraw(String accountNumber, BigDecimal amount, String description) throws BankingException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Withdrawal amount must be positive");
        }
        
        BankAccount account = getAccountForCurrentUser(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BankingException("Insufficient funds");
        }
        
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, accountNumber, 
                Transaction.TransactionType.WITHDRAWAL, amount, newBalance, description);
        transactions.add(transaction);
        
        saveData();
    }
    
    public List<BankAccount> getUserAccounts() {
        if (currentUser == null) return new ArrayList<>();
        
        return accounts.values().stream()
                .filter(account -> account.getUsername().equals(currentUser.getUsername()) && account.isActive())
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getAccountTransactions(String accountNumber) throws BankingException {
        getAccountForCurrentUser(accountNumber);
        
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .collect(Collectors.toList());
    }
    
    private BankAccount getAccountForCurrentUser(String accountNumber) throws BankingException {
        BankAccount account = accounts.get(accountNumber);
        if (account == null || !account.isActive()) {
            throw new BankingException("Account not found");
        }
        if (!account.getUsername().equals(currentUser.getUsername())) {
            throw new BankingException("Access denied");
        }
        return account;
    }
    
    private String generateAccountNumber() {
        return "ACC" + String.format("%08d", new Random().nextInt(100000000));
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
}