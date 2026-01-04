package com.challenge.banking;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Main bank class that manages multiple accounts and provides banking operations.
 * Handles account creation, transactions, and reporting functionality.
 */
public class Bank {
    private final Map<String, Account> accounts;
    private static int accountCounter = 1000; // Starting account number
    
    /**
     * Constructor initializes the bank with an empty accounts map
     */
    public Bank() {
        this.accounts = new HashMap<>();
    }
    
    /**
     * Generate a unique account number
     */
    private synchronized String generateAccountNumber() {
        return String.valueOf(++accountCounter);
    }
    
    /**
     * Open a new account with initial deposit
     * 
     * @param customerName Name of the customer
     * @param accountType Type of account (CHECKING or SAVINGS)
     * @param initialDeposit Initial deposit amount
     * @return Account number of the newly created account
     * @throws BankingException if initial deposit is invalid or violates account rules
     */
    public String openAccount(String customerName, AccountType accountType, double initialDeposit) throws BankingException {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new BankingException("Customer name cannot be empty");
        }
        
        if (initialDeposit < 0) {
            throw new BankingException("Initial deposit cannot be negative");
        }
        
        // For savings accounts, check minimum balance requirement
        if (accountType == AccountType.SAVINGS && initialDeposit < 100.00) {
            throw new BankingException("Savings account requires minimum initial deposit of $100.00");
        }
        
        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, accountType, customerName, initialDeposit);
        accounts.put(accountNumber, account);
        
        return accountNumber;
    }
    
    /**
     * Close an account (only if balance is zero)
     * 
     * @param accountNumber Account number to close
     * @throws BankingException if account doesn't exist or has non-zero balance
     */
    public void closeAccount(String accountNumber) throws BankingException {
        Account account = getAccount(accountNumber);
        
        if (Math.abs(account.getBalance()) > 0.01) { // Allow for small floating point differences
            throw new BankingException("Cannot close account with non-zero balance. Current balance: $" + 
                    String.format("%.2f", account.getBalance()));
        }
        
        accounts.remove(accountNumber);
    }
    
    /**
     * Get account by account number
     * 
     * @param accountNumber Account number to retrieve
     * @return Account object
     * @throws BankingException if account doesn't exist
     */
    private Account getAccount(String accountNumber) throws BankingException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new BankingException("Account not found: " + accountNumber);
        }
        return account;
    }
    
    /**
     * Deposit money into an account
     * 
     * @param accountNumber Target account number
     * @param amount Amount to deposit
     * @return Transaction object representing the deposit
     * @throws BankingException if account doesn't exist
     */
    public Transaction deposit(String accountNumber, double amount) throws BankingException {
        Account account = getAccount(accountNumber);
        return account.deposit(amount);
    }
    
    /**
     * Withdraw money from an account
     * 
     * @param accountNumber Source account number
     * @param amount Amount to withdraw
     * @return Transaction object representing the withdrawal
     * @throws BankingException if account doesn't exist
     */
    public Transaction withdraw(String accountNumber, double amount) throws BankingException {
        Account account = getAccount(accountNumber);
        return account.withdraw(amount);
    }
    
    /**
     * Transfer money between accounts
     * 
     * @param fromAccountNumber Source account number
     * @param toAccountNumber Destination account number
     * @param amount Amount to transfer
     * @return Array of two transactions: [withdrawal from source, deposit to destination]
     * @throws BankingException if either account doesn't exist or transfer fails
     */
    public Transaction[] transfer(String fromAccountNumber, String toAccountNumber, double amount) throws BankingException {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new BankingException("Cannot transfer to the same account");
        }
        
        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);
        
        // Attempt withdrawal first
        Transaction withdrawalTransaction = fromAccount.withdraw(amount);
        
        // If withdrawal failed, return failed transactions
        if (withdrawalTransaction.getStatus() == Transaction.TransactionStatus.FAILED) {
            // Create a failed transfer transaction for the destination account
            Transaction failedDeposit = new Transaction(
                Transaction.TransactionType.TRANSFER,
                amount,
                toAccount.getBalance(),
                "Transfer failed: " + withdrawalTransaction.getFailureReason()
            );
            toAccount.getTransactionHistory().add(failedDeposit);
            
            return new Transaction[]{withdrawalTransaction, failedDeposit};
        }
        
        // If withdrawal succeeded, process deposit
        Transaction depositTransaction = toAccount.deposit(amount);
        
        // Convert transactions to TRANSFER type for proper tracking
        Transaction transferOut = new Transaction(
            Transaction.TransactionType.TRANSFER,
            -amount, // Negative for outgoing transfer
            withdrawalTransaction.getBalanceBefore(),
            withdrawalTransaction.getBalanceAfter()
        );
        
        Transaction transferIn = new Transaction(
            Transaction.TransactionType.TRANSFER,
            amount, // Positive for incoming transfer
            depositTransaction.getBalanceBefore(),
            depositTransaction.getBalanceAfter()
        );
        
        // Replace the last transactions in history with transfer transactions
        fromAccount.getTransactionHistory().remove(fromAccount.getTransactionHistory().size() - 1);
        toAccount.getTransactionHistory().remove(toAccount.getTransactionHistory().size() - 1);
        
        fromAccount.getTransactionHistory().add(transferOut);
        toAccount.getTransactionHistory().add(transferIn);
        
        return new Transaction[]{transferOut, transferIn};
    }
    
    /**
     * Get transaction history for an account within a date range
     * 
     * @param accountNumber Account number
     * @param startDate Start date for the range
     * @param endDate End date for the range
     * @return List of transactions within the date range
     * @throws BankingException if account doesn't exist
     */
    public List<Transaction> getTransactionHistory(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) throws BankingException {
        Account account = getAccount(accountNumber);
        return account.getTransactionHistory(startDate, endDate);
    }
    
    /**
     * Get all transaction history for an account
     * 
     * @param accountNumber Account number
     * @return List of all transactions
     * @throws BankingException if account doesn't exist
     */
    public List<Transaction> getTransactionHistory(String accountNumber) throws BankingException {
        Account account = getAccount(accountNumber);
        return account.getTransactionHistory();
    }
    
    /**
     * Apply monthly interest to all savings accounts
     */
    public void applyMonthlyInterest() {
        for (Account account : accounts.values()) {
            if (account.getAccountType() == AccountType.SAVINGS) {
                account.applyMonthlyInterest();
            }
        }
    }
    
    /**
     * Generate monthly statement for an account
     * 
     * @param accountNumber Account number
     * @return Formatted monthly statement
     * @throws BankingException if account doesn't exist
     */
    public String generateMonthlyStatement(String accountNumber) throws BankingException {
        Account account = getAccount(accountNumber);
        return account.generateMonthlyStatement();
    }
    
    /**
     * Get account balance
     * 
     * @param accountNumber Account number
     * @return Current account balance
     * @throws BankingException if account doesn't exist
     */
    public double getAccountBalance(String accountNumber) throws BankingException {
        Account account = getAccount(accountNumber);
        return account.getBalance();
    }
    
    /**
     * Get account information
     * 
     * @param accountNumber Account number
     * @return Account object
     * @throws BankingException if account doesn't exist
     */
    public Account getAccountInfo(String accountNumber) throws BankingException {
        return getAccount(accountNumber);
    }
    
    /**
     * Get all accounts (for administrative purposes)
     * 
     * @return Map of all accounts
     */
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>(accounts);
    }
    
    /**
     * Get total number of accounts
     * 
     * @return Number of accounts
     */
    public int getAccountCount() {
        return accounts.size();
    }
}