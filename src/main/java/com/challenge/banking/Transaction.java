package com.challenge.banking;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a bank transaction with all necessary details for tracking and auditing.
 * Immutable class that captures transaction state at the time of creation.
 */
public class Transaction {
    private final String transactionId;
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final double balanceBefore;
    private final double balanceAfter;
    private final TransactionStatus status;
    private final String failureReason;
    
    /**
     * Enumeration of supported transaction types
     */
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }
    
    /**
     * Enumeration of transaction status
     */
    public enum TransactionStatus {
        SUCCESS, FAILED
    }
    
    /**
     * Constructor for successful transactions
     */
    public Transaction(TransactionType type, double amount, double balanceBefore, double balanceAfter) {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.status = TransactionStatus.SUCCESS;
        this.failureReason = null;
    }
    
    /**
     * Constructor for failed transactions
     */
    public Transaction(TransactionType type, double amount, double balanceBefore, String failureReason) {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceBefore; // Balance remains unchanged for failed transactions
        this.status = TransactionStatus.FAILED;
        this.failureReason = failureReason;
    }
    
    // Getters
    public String getTransactionId() {
        return transactionId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getBalanceBefore() {
        return balanceBefore;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
    
    @Override
    public String toString() {
        return String.format("Transaction{id='%s', timestamp=%s, type=%s, amount=%.2f, balanceBefore=%.2f, balanceAfter=%.2f, status=%s%s}",
                transactionId, timestamp, type, amount, balanceBefore, balanceAfter, status,
                failureReason != null ? ", reason='" + failureReason + "'" : "");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return transactionId.equals(that.transactionId);
    }
    
    @Override
    public int hashCode() {
        return transactionId.hashCode();
    }
}