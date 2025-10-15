package BankingApp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String accountNumber;
    private String username;
    private BigDecimal balance;
    private AccountType accountType;
    private LocalDateTime createdAt;
    private boolean isActive;
    
    public enum AccountType {
        SAVINGS, CHECKING, CURRENT
    }
    
    public BankAccount(String accountNumber, String username, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.accountType = accountType;
        this.balance = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public String getUsername() { return username; }
    public BigDecimal getBalance() { return balance; }
    public AccountType getAccountType() { return accountType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }
    
    public void setBalance(BigDecimal balance) { 
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            this.balance = balance; 
        }
    }
    
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return String.format("BankAccount{accountNumber='%s', username='%s', balance=%.2f, type=%s}", 
                           accountNumber, username, balance, accountType);
    }
}