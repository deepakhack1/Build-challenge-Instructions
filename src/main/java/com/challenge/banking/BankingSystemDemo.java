package com.challenge.banking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Demonstration class showing the banking system with 4 accounts and 20+ transactions
 * including failed attempts to showcase all functionality and error handling.
 */
public class BankingSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Banking System Demonstration ===");
        System.out.println("Demonstrating 4 accounts with 20+ transactions including failures\n");
        
        Bank bank = new Bank();
        
        try {
            // Create 4 accounts: 2 checking, 2 savings
            System.out.println("1. Creating Accounts:");
            String checkingAccount1 = bank.openAccount("John Doe", AccountType.CHECKING, 500.00);
            System.out.println("   Checking Account 1 created: " + checkingAccount1 + " with $500.00");
            
            String checkingAccount2 = bank.openAccount("Jane Smith", AccountType.CHECKING, 1000.00);
            System.out.println("   Checking Account 2 created: " + checkingAccount2 + " with $1000.00");
            
            String savingsAccount1 = bank.openAccount("Bob Johnson", AccountType.SAVINGS, 2000.00);
            System.out.println("   Savings Account 1 created: " + savingsAccount1 + " with $2000.00");
            
            String savingsAccount2 = bank.openAccount("Alice Brown", AccountType.SAVINGS, 150.00);
            System.out.println("   Savings Account 2 created: " + savingsAccount2 + " with $150.00");
            
            System.out.println("\n2. Performing Transactions:");
            
            // Transaction 1-3: Successful deposits
            performTransaction(bank, "Deposit $200 to Checking Account 1", 
                () -> bank.deposit(checkingAccount1, 200.00));
            
            performTransaction(bank, "Deposit $300 to Savings Account 1", 
                () -> bank.deposit(savingsAccount1, 300.00));
            
            performTransaction(bank, "Deposit $50 to Checking Account 2", 
                () -> bank.deposit(checkingAccount2, 50.00));
            
            // Transaction 4-6: Successful withdrawals
            performTransaction(bank, "Withdraw $100 from Checking Account 1", 
                () -> bank.withdraw(checkingAccount1, 100.00));
            
            performTransaction(bank, "Withdraw $50 from Savings Account 1", 
                () -> bank.withdraw(savingsAccount1, 50.00));
            
            performTransaction(bank, "Withdraw $25 from Savings Account 2", 
                () -> bank.withdraw(savingsAccount2, 25.00));
            
            // Transaction 7-9: Successful transfers
            performTransfer(bank, "Transfer $150 from Checking 1 to Checking 2", 
                checkingAccount1, checkingAccount2, 150.00);
            
            performTransfer(bank, "Transfer $100 from Savings 1 to Savings 2", 
                savingsAccount1, savingsAccount2, 100.00);
            
            performTransfer(bank, "Transfer $200 from Checking 2 to Savings 1", 
                checkingAccount2, savingsAccount1, 200.00);
            
            // Transaction 10-12: Failed transactions - insufficient funds
            performTransaction(bank, "Attempt to withdraw $2000 from Checking Account 1 (should fail)", 
                () -> bank.withdraw(checkingAccount1, 2000.00));
            
            performTransaction(bank, "Attempt to withdraw below minimum balance from Savings Account 2 (should fail)", 
                () -> bank.withdraw(savingsAccount2, 200.00));
            
            performTransfer(bank, "Attempt transfer $5000 from Checking 1 to Checking 2 (should fail)", 
                checkingAccount1, checkingAccount2, 5000.00);
            
            // Transaction 13-15: Test checking account transaction fees (after 10 transactions)
            System.out.println("\n   Testing Checking Account Transaction Fees:");
            for (int i = 1; i <= 8; i++) {
                performTransaction(bank, "Small deposit #" + i + " to Checking Account 1 (testing fee threshold)", 
                    () -> bank.deposit(checkingAccount1, 10.00));
            }
            
            // This should trigger the fee (11th transaction)
            performTransaction(bank, "Deposit triggering transaction fee (11th transaction)", 
                () -> bank.deposit(checkingAccount1, 10.00));
            
            // Transaction 16-20: Test savings account withdrawal limits
            System.out.println("\n   Testing Savings Account Withdrawal Limits:");
            for (int i = 1; i <= 4; i++) {
                performTransaction(bank, "Withdrawal #" + (i+1) + " from Savings Account 1 (testing limit)", 
                    () -> bank.withdraw(savingsAccount1, 20.00));
            }
            
            // This should fail (6th withdrawal)
            performTransaction(bank, "Attempt 6th withdrawal from Savings Account 1 (should fail)", 
                () -> bank.withdraw(savingsAccount1, 20.00));
            
            // Transaction 21-22: Failed deposits with negative amounts
            performTransaction(bank, "Attempt negative deposit (should fail)", 
                () -> bank.deposit(checkingAccount1, -50.00));
            
            performTransaction(bank, "Attempt zero withdrawal (should fail)", 
                () -> bank.withdraw(savingsAccount1, 0.00));
            
            // Apply monthly interest
            System.out.println("\n3. Applying Monthly Interest to Savings Accounts:");
            bank.applyMonthlyInterest();
            System.out.println("   Monthly interest applied to all savings accounts");
            
            // Display final account balances
            System.out.println("\n4. Final Account Balances:");
            displayAccountBalance(bank, checkingAccount1, "Checking Account 1");
            displayAccountBalance(bank, checkingAccount2, "Checking Account 2");
            displayAccountBalance(bank, savingsAccount1, "Savings Account 1");
            displayAccountBalance(bank, savingsAccount2, "Savings Account 2");
            
            // Generate monthly statements
            System.out.println("\n5. Monthly Statements:");
            System.out.println(bank.generateMonthlyStatement(checkingAccount1));
            System.out.println(bank.generateMonthlyStatement(savingsAccount1));
            
            // Test account closing
            System.out.println("\n6. Testing Account Closure:");
            try {
                bank.closeAccount(checkingAccount1);
                System.out.println("   Attempted to close account with balance (should fail)");
            } catch (BankingException e) {
                System.out.println("   ✓ Account closure failed as expected: " + e.getMessage());
            }
            
            // Test transaction history
            System.out.println("\n7. Transaction History Sample:");
            List<Transaction> history = bank.getTransactionHistory(checkingAccount1);
            System.out.println("   Total transactions for Checking Account 1: " + history.size());
            System.out.println("   Last 3 transactions:");
            int start = Math.max(0, history.size() - 3);
            for (int i = start; i < history.size(); i++) {
                System.out.println("   " + history.get(i));
            }
            
            System.out.println("\n=== Demonstration Complete ===");
            System.out.println("Total accounts created: " + bank.getAccountCount());
            System.out.println("All banking operations demonstrated successfully!");
            
        } catch (BankingException e) {
            System.err.println("Banking error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to perform and log a transaction
     */
    private static void performTransaction(Bank bank, String description, TransactionOperation operation) {
        try {
            System.out.println("   " + description);
            Transaction transaction = operation.execute();
            if (transaction.getStatus() == Transaction.TransactionStatus.SUCCESS) {
                System.out.println("     ✓ SUCCESS: Balance after: $" + String.format("%.2f", transaction.getBalanceAfter()));
            } else {
                System.out.println("     ✗ FAILED: " + transaction.getFailureReason());
            }
        } catch (BankingException e) {
            System.out.println("     ✗ ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to perform and log a transfer
     */
    private static void performTransfer(Bank bank, String description, String fromAccount, String toAccount, double amount) {
        try {
            System.out.println("   " + description);
            Transaction[] transactions = bank.transfer(fromAccount, toAccount, amount);
            if (transactions[0].getStatus() == Transaction.TransactionStatus.SUCCESS) {
                System.out.println("     ✓ SUCCESS: From balance: $" + String.format("%.2f", transactions[0].getBalanceAfter()) + 
                                 ", To balance: $" + String.format("%.2f", transactions[1].getBalanceAfter()));
            } else {
                System.out.println("     ✗ FAILED: " + transactions[0].getFailureReason());
            }
        } catch (BankingException e) {
            System.out.println("     ✗ ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to display account balance
     */
    private static void displayAccountBalance(Bank bank, String accountNumber, String accountName) {
        try {
            double balance = bank.getAccountBalance(accountNumber);
            Account account = bank.getAccountInfo(accountNumber);
            System.out.println("   " + accountName + " (" + accountNumber + "): $" + 
                             String.format("%.2f", balance) + " [" + account.getAccountType() + "]");
        } catch (BankingException e) {
            System.out.println("   " + accountName + ": Error retrieving balance - " + e.getMessage());
        }
    }
    
    /**
     * Functional interface for transaction operations
     */
    @FunctionalInterface
    private interface TransactionOperation {
        Transaction execute() throws BankingException;
    }
}