package BankingApp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileDataManager {
    private static final String USERS_FILE = "data/users.txt";
    private static final String ACCOUNTS_FILE = "data/accounts.txt";
    private static final String TRANSACTIONS_FILE = "data/transactions.txt";
    
    static {
        new File("data").mkdirs();
    }
    
    public static void saveUsers(List<User> users) {
        saveObjects(users, USERS_FILE);
    }
    
    public static List<User> loadUsers() {
        return loadObjects(USERS_FILE).stream()
                .map(obj -> (User) obj)
                .collect(Collectors.toList());
    }
    
    public static void saveAccounts(List<BankAccount> accounts) {
        saveObjects(accounts, ACCOUNTS_FILE);
    }
    
    public static List<BankAccount> loadAccounts() {
        return loadObjects(ACCOUNTS_FILE).stream()
                .map(obj -> (BankAccount) obj)
                .collect(Collectors.toList());
    }
    
    public static void saveTransactions(List<Transaction> transactions) {
        saveObjects(transactions, TRANSACTIONS_FILE);
    }
    
    public static List<Transaction> loadTransactions() {
        return loadObjects(TRANSACTIONS_FILE).stream()
                .map(obj -> (Transaction) obj)
                .collect(Collectors.toList());
    }
    
    private static void saveObjects(List<?> objects, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            for (Object obj : objects) {
                oos.writeObject(DataEncryptor.encryptObject(obj));
            }
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
        }
    }
    
    private static List<Object> loadObjects(String filename) {
        List<Object> objects = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) {
            return objects;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            while (true) {
                try {
                    byte[] encryptedData = (byte[]) ois.readObject();
                    Object obj = DataEncryptor.decryptObject(encryptedData);
                    objects.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
        }
        
        return objects;
    }
}