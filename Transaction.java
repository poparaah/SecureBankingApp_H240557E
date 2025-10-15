package BankingApp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime timestamp;
    
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }
    
    public Transaction(String transactionId, String accountNumber, TransactionType type, 
                      BigDecimal amount, BigDecimal balanceAfter, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = sanitizeInput(description);
        this.timestamp = LocalDateTime.now();
    }
    
    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>\"']", "");
    }
    
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("Transaction{id='%s', account='%s', type=%s, amount=%.2f, balanceAfter=%.2f, desc='%s'}", 
                           transactionId, accountNumber, type, amount, balanceAfter, description);
    }
}