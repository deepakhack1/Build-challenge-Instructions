package com.challenge.banking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a bank account with transaction history and account-specific rules.
 * Supports both CHECKING and SAVINGS account types with different fee structures and constraints.
 */
public class Account {
    private final String accountNumber;
    private final AccountType accountType;
    private final String customerName;
    private double balance;
    private final List<Transaction> transactionHistory;
    private int monthlyTransactionCount;
    private int monthlyWithdrawalCount; // For savings accounts
    private LocalDate lastResetDate; // Track when monthly counters were last reset
    
    // Constants for account rules
    private static final double CHECKING_TRANSACTION_FEE = 2.50;
    private static final int CHECKING_FREE_TRANSACTIONS = 10;
    private static final double SAVINGS_MINIMUM_BALANCE = 100.00;
    private static final double SAVINGS_MONTHLY_INTEREST_RATE = 0.02; // 2%
    private static final int SAVINGS_MAX_WITHDRAWALS = 5;
    
    /**
     * Constructor for creating a new account
     */
    public Account(String accountNumber, AccountType accountType, String customerName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.customerName = customerName;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.monthlyTransactionCount = 0;
        this.monthlyWithdrawalCount = 0;
        this.lastResetDate = LocalDate.now();

        // Record initial deposit transaction (this doesn't count towards monthly transaction limit)
        if (initialDeposit > 0) {
            Transaction initialTransaction = new Transaction(
                    Transaction.TransactionType.DEPOSIT,
                    initialDeposit,
                    0.0,
                    initialDeposit
            );
            transactionHistory.add(initialTransaction);
        }
    }
    
    /**
     * Reset monthly counters if a new month has started
     */
    private void checkAndResetMonthlyCounters() {
        LocalDate currentDate = LocalDate.now();
        if (!currentDate.getMonth().equals(lastResetDate.getMonth()) ||
                !(currentDate.getYear() == lastResetDate.getYear())) {
            monthlyTransactionCount = 0;
            monthlyWithdrawalCount = 0;
            lastResetDate = currentDate;
        }
    }

    /**
     * Validate if a withdrawal is allowed based on account type rules
     */
    private String validateWithdrawal(double amount) {
        checkAndResetMonthlyCounters();

        if (amount <= 0) {
            return "Withdrawal amount must be positive";
        }

        // Check if withdrawal would result in negative balance
        double potentialBalance = balance - amount;

        // For checking accounts, apply transaction fee if over free limit
        // Note: We check if the NEXT transaction (monthlyTransactionCount + 1) would exceed the free limit
        if (accountType == AccountType.CHECKING && (monthlyTransactionCount + 1) > CHECKING_FREE_TRANSACTIONS) {
            potentialBalance -= CHECKING_TRANSACTION_FEE;
        }

        if (potentialBalance < 0) {
            return "Insufficient funds";
        }

        // For savings accounts, check minimum balance and withdrawal limits
        if (accountType == AccountType.SAVINGS) {
            if (potentialBalance < SAVINGS_MINIMUM_BALANCE) {
                return "Withdrawal would violate minimum balance requirement of $" + SAVINGS_MINIMUM_BALANCE;
            }
            if (monthlyWithdrawalCount >= SAVINGS_MAX_WITHDRAWALS) {
                return "Monthly withdrawal limit exceeded (max " + SAVINGS_MAX_WITHDRAWALS + " withdrawals)";
            }
        }

        return null; // No validation errors
    }
    
    /**
     * Validate if a deposit is allowed
     */
    private String validateDeposit(double amount) {
        if (amount <= 0) {
            return "Deposit amount must be positive";
        }
        return null; // No validation errors
    }

    /**
     * Process a deposit transaction
     */
    public Transaction deposit(double amount) {
        checkAndResetMonthlyCounters();

        String validationError = validateDeposit(amount);
        if (validationError != null) {
            Transaction failedTransaction = new Transaction(
                    Transaction.TransactionType.DEPOSIT,
                    amount,
                    balance,
                    validationError
            );
            transactionHistory.add(failedTransaction);
            return failedTransaction;
        }

        double oldBalance = balance;
        balance += amount;
        monthlyTransactionCount++;

        // Apply transaction fee for checking accounts if over free limit
        if (accountType == AccountType.CHECKING && monthlyTransactionCount > CHECKING_FREE_TRANSACTIONS) {
            balance -= CHECKING_TRANSACTION_FEE;
        }

        Transaction transaction = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                amount,
                oldBalance,
                balance
        );
        transactionHistory.add(transaction);

        return transaction;
    }
    
    /**
     * Process a withdrawal transaction
     */
    public Transaction withdraw(double amount) {
        checkAndResetMonthlyCounters();
        
        String validationError = validateWithdrawal(amount);
        if (validationError != null) {
            Transaction failedTransaction = new Transaction(
                Transaction.TransactionType.WITHDRAWAL, 
                amount, 
                balance, 
                validationError
            );
            transactionHistory.add(failedTransaction);
            return failedTransaction;
        }
        
        double oldBalance = balance;
        balance -= amount;
        monthlyTransactionCount++;
        
        if (accountType == AccountType.SAVINGS) {
            monthlyWithdrawalCount++;
        }
        
        // Apply transaction fee for checking accounts if over free limit
        if (accountType == AccountType.CHECKING && monthlyTransactionCount > CHECKING_FREE_TRANSACTIONS) {
            balance -= CHECKING_TRANSACTION_FEE;
        }
        
        Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAWAL, 
            amount, 
            oldBalance, 
            balance
        );
        transactionHistory.add(transaction);
        
        return transaction;
    }
    
    /**
     * Apply monthly interest for savings accounts
     */
    public void applyMonthlyInterest() {
        if (accountType == AccountType.SAVINGS && balance > 0) {
            double oldBalance = balance;
            double interest = balance * SAVINGS_MONTHLY_INTEREST_RATE;
            balance += interest;
            
            Transaction interestTransaction = new Transaction(
                Transaction.TransactionType.DEPOSIT, 
                interest, 
                oldBalance, 
                balance
            );
            transactionHistory.add(interestTransaction);
        }
    }
    
    /**
     * Get transaction history within a date range
     */
    public List<Transaction> getTransactionHistory(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionHistory.stream()
                .filter(t -> !t.getTimestamp().isBefore(startDate) && !t.getTimestamp().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all transaction history
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    /**
     * Generate a monthly statement
     */
    public String generateMonthlyStatement() {
        checkAndResetMonthlyCounters();
        
        StringBuilder statement = new StringBuilder();
        statement.append("\n=== MONTHLY STATEMENT ===").append("\n");
        statement.append("Account Number: ").append(accountNumber).append("\n");
        statement.append("Customer Name: ").append(customerName).append("\n");
        statement.append("Account Type: ").append(accountType).append("\n");
        statement.append("Current Balance: $").append(String.format("%.2f", balance)).append("\n");
        statement.append("Monthly Transaction Count: ").append(monthlyTransactionCount).append("\n");
        
        if (accountType == AccountType.SAVINGS) {
            statement.append("Monthly Withdrawal Count: ").append(monthlyWithdrawalCount).append("\n");
        }
        
        statement.append("\nTransaction History:\n");
        for (Transaction transaction : transactionHistory) {
            statement.append(transaction.toString()).append("\n");
        }
        
        return statement.toString();
    }
    
    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public int getMonthlyTransactionCount() {
        checkAndResetMonthlyCounters();
        return monthlyTransactionCount;
    }
    
    public int getMonthlyWithdrawalCount() {
        checkAndResetMonthlyCounters();
        return monthlyWithdrawalCount;
    }
    
    @Override
    public String toString() {
        return String.format("Account{accountNumber='%s', type=%s, customer='%s', balance=%.2f}",
                accountNumber, accountType, customerName, balance);
    }
}