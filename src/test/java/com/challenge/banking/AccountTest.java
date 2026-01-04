package com.challenge.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Comprehensive unit tests for the Account class.
 * Tests account creation, deposit/withdrawal operations, fees, limits, and reporting.
 */
class AccountTest {
    
    private String accountNumber;
    private String customerName;
    private double initialDeposit;
    
    @BeforeEach
    void setUp() {
        accountNumber = "12345";
        customerName = "John Doe";
        initialDeposit = 500.0;
    }
    
    /**
     * Test account creation with checking account type.
     * Verifies that checking account is created with correct initial values.
     */
    @Test
    @DisplayName("Should create checking account with correct initial values")
    void testCheckingAccountCreation() {
        // Arrange & Act
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        
        // Assert
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(customerName, account.getCustomerName());
        assertEquals(initialDeposit, account.getBalance());
        assertEquals(0, account.getMonthlyTransactionCount());
        assertEquals(0, account.getMonthlyWithdrawalCount());
        assertEquals(1, account.getTransactionHistory().size()); // Initial deposit transaction
    }
    
    /**
     * Test account creation with savings account type.
     * Verifies that savings account is created with correct initial values.
     */
    @Test
    @DisplayName("Should create savings account with correct initial values")
    void testSavingsAccountCreation() {
        // Arrange & Act
        Account account = new Account(accountNumber, AccountType.SAVINGS, customerName, initialDeposit);
        
        // Assert
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(customerName, account.getCustomerName());
        assertEquals(initialDeposit, account.getBalance());
        assertEquals(0, account.getMonthlyTransactionCount());
        assertEquals(0, account.getMonthlyWithdrawalCount());
        assertEquals(1, account.getTransactionHistory().size()); // Initial deposit transaction
    }
    
    /**
     * Test account creation with zero initial deposit.
     * Verifies that account can be created with zero initial deposit.
     */
    @Test
    @DisplayName("Should create account with zero initial deposit")
    void testAccountCreationWithZeroDeposit() {
        // Arrange & Act
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, 0.0);
        
        // Assert
        assertEquals(0.0, account.getBalance());
        assertEquals(0, account.getTransactionHistory().size()); // No initial transaction for zero deposit
    }
    
    /**
     * Test successful deposit operation.
     * Verifies that valid deposit increases balance and creates transaction record.
     */
    @Test
    @DisplayName("Should process successful deposit and update balance")
    void testSuccessfulDeposit() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        double depositAmount = 100.0;
        double expectedBalance = initialDeposit + depositAmount;
        
        // Act
        Transaction transaction = account.deposit(depositAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(Transaction.TransactionType.DEPOSIT, transaction.getType());
        assertEquals(depositAmount, transaction.getAmount());
        assertEquals(expectedBalance, account.getBalance());
        assertEquals(2, account.getTransactionHistory().size()); // Initial + deposit
    }
    
    /**
     * Test failed deposit with negative amount.
     * Verifies that negative deposit amount is rejected.
     */
    @Test
    @DisplayName("Should reject deposit with negative amount")
    void testFailedDepositWithNegativeAmount() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        double negativeAmount = -50.0;
        
        // Act
        Transaction transaction = account.deposit(negativeAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.getStatus());
        assertEquals("Deposit amount must be positive", transaction.getFailureReason());
        assertEquals(initialDeposit, account.getBalance()); // Balance unchanged
    }
    
    /**
     * Test failed deposit with zero amount.
     * Verifies that zero deposit amount is rejected.
     */
    @Test
    @DisplayName("Should reject deposit with zero amount")
    void testFailedDepositWithZeroAmount() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        
        // Act
        Transaction transaction = account.deposit(0.0);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.getStatus());
        assertEquals("Deposit amount must be positive", transaction.getFailureReason());
        assertEquals(initialDeposit, account.getBalance());
    }
    
    /**
     * Test successful withdrawal operation.
     * Verifies that valid withdrawal decreases balance and creates transaction record.
     */
    @Test
    @DisplayName("Should process successful withdrawal and update balance")
    void testSuccessfulWithdrawal() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        double withdrawalAmount = 100.0;
        double expectedBalance = initialDeposit - withdrawalAmount;
        
        // Act
        Transaction transaction = account.withdraw(withdrawalAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals(withdrawalAmount, transaction.getAmount());
        assertEquals(expectedBalance, account.getBalance());
    }
    
    /**
     * Test failed withdrawal with insufficient funds.
     * Verifies that withdrawal exceeding balance is rejected.
     */
    @Test
    @DisplayName("Should reject withdrawal with insufficient funds")
    void testFailedWithdrawalInsufficientFunds() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, 100.0);
        double excessiveAmount = 200.0;
        
        // Act
        Transaction transaction = account.withdraw(excessiveAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.getStatus());
        assertEquals("Insufficient funds", transaction.getFailureReason());
        assertEquals(100.0, account.getBalance()); // Balance unchanged
    }
    
    /**
     * Test failed withdrawal with negative amount.
     * Verifies that negative withdrawal amount is rejected.
     */
    @Test
    @DisplayName("Should reject withdrawal with negative amount")
    void testFailedWithdrawalNegativeAmount() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        
        // Act
        Transaction transaction = account.withdraw(-50.0);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.getStatus());
        assertEquals("Withdrawal amount must be positive", transaction.getFailureReason());
        assertEquals(initialDeposit, account.getBalance());
    }
    
    /**
     * Test checking account transaction fees after free limit.
     * Verifies that checking accounts charge fees after 10 free transactions.
     */
    @Test
    @DisplayName("Should apply transaction fees for checking account after free limit")
    void testCheckingAccountTransactionFees() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, 1000.0);
        double transactionFee = 2.50;
        
        // Act - Perform 10 transactions (free limit)
        for (int i = 0; i < 10; i++) {
            account.deposit(10.0);
        }
        double balanceAfterFreeTransactions = account.getBalance();
        
        // Perform 11th transaction (should incur fee)
        account.deposit(10.0);
        
        // Assert
        double expectedBalance = balanceAfterFreeTransactions + 10.0 - transactionFee;
        assertEquals(expectedBalance, account.getBalance(), 0.01);
        assertEquals(11, account.getMonthlyTransactionCount());
    }
    
    /**
     * Test savings account minimum balance requirement for withdrawal.
     * Verifies that savings account withdrawal is rejected if it violates minimum balance.
     */
    @Test
    @DisplayName("Should reject savings account withdrawal violating minimum balance")
    void testSavingsAccountMinimumBalanceRule() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.SAVINGS, customerName, 150.0);
        double withdrawalAmount = 100.0; // Would leave balance at 50, below minimum of 100
        
        // Act
        Transaction transaction = account.withdraw(withdrawalAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.getStatus());
        assertTrue(transaction.getFailureReason().contains("minimum balance requirement"));
        assertEquals(150.0, account.getBalance());
    }
    
    /**
     * Test savings account withdrawal limits.
     * Verifies that savings account rejects withdrawals after monthly limit.
     */
    @Test
    @DisplayName("Should reject savings account withdrawal after monthly limit")
    void testSavingsAccountWithdrawalLimits() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.SAVINGS, customerName, 1000.0);
        
        // Act - Perform 5 withdrawals (maximum allowed)
        for (int i = 0; i < 5; i++) {
            Transaction transaction = account.withdraw(50.0);
            assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        }
        
        // Attempt 6th withdrawal (should fail)
        Transaction failedTransaction = account.withdraw(50.0);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.FAILED, failedTransaction.getStatus());
        assertTrue(failedTransaction.getFailureReason().contains("withdrawal limit exceeded"));
        assertEquals(5, account.getMonthlyWithdrawalCount());
    }
    
    /**
     * Test monthly interest application for savings account.
     * Verifies that savings accounts earn monthly interest.
     */
    @Test
    @DisplayName("Should apply monthly interest to savings account")
    void testMonthlyInterestApplication() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.SAVINGS, customerName, 1000.0);
        double interestRate = 0.02; // 2%
        double expectedInterest = 1000.0 * interestRate;
        double expectedBalance = 1000.0 + expectedInterest;
        
        // Act
        account.applyMonthlyInterest();
        
        // Assert
        assertEquals(expectedBalance, account.getBalance(), 0.01);
        assertEquals(2, account.getTransactionHistory().size()); // Initial deposit + interest
    }
    
    /**
     * Test that checking account does not earn interest.
     * Verifies that interest is not applied to checking accounts.
     */
    @Test
    @DisplayName("Should not apply interest to checking account")
    void testNoInterestForCheckingAccount() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, 1000.0);
        double initialBalance = account.getBalance();
        
        // Act
        account.applyMonthlyInterest();
        
        // Assert
        assertEquals(initialBalance, account.getBalance());
        assertEquals(1, account.getTransactionHistory().size()); // Only initial deposit
    }
    
    /**
     * Test transaction history retrieval.
     * Verifies that all transactions are properly recorded and retrievable.
     */
    @Test
    @DisplayName("Should maintain and retrieve complete transaction history")
    void testTransactionHistoryRetrieval() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        
        // Act
        account.deposit(100.0);
        account.withdraw(50.0);
        account.deposit(25.0);
        
        List<Transaction> history = account.getTransactionHistory();
        
        // Assert
        assertEquals(4, history.size()); // Initial deposit + 3 transactions
        assertEquals(Transaction.TransactionType.DEPOSIT, history.get(0).getType()); // Initial
        assertEquals(Transaction.TransactionType.DEPOSIT, history.get(1).getType());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, history.get(2).getType());
        assertEquals(Transaction.TransactionType.DEPOSIT, history.get(3).getType());
    }
    
    /**
     * Test transaction history retrieval with date range.
     * Verifies that transactions can be filtered by date range.
     */
    @Test
    @DisplayName("Should retrieve transaction history within date range")
    void testTransactionHistoryWithDateRange() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        
        // Act
        account.deposit(100.0);
        account.withdraw(50.0);
        
        List<Transaction> history = account.getTransactionHistory(startDate, endDate);
        
        // Assert
        assertEquals(3, history.size()); // All transactions should be within range
    }
    
    /**
     * Test monthly statement generation.
     * Verifies that monthly statement contains all required information.
     */
    @Test
    @DisplayName("Should generate comprehensive monthly statement")
    void testMonthlyStatementGeneration() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.SAVINGS, customerName, initialDeposit);
        account.deposit(100.0);
        account.withdraw(50.0);
        
        // Act
        String statement = account.generateMonthlyStatement();
        
        // Assert
        assertTrue(statement.contains("MONTHLY STATEMENT"));
        assertTrue(statement.contains("Account Number: " + accountNumber));
        assertTrue(statement.contains("Customer Name: " + customerName));
        assertTrue(statement.contains("Account Type: SAVINGS"));
        assertTrue(statement.contains("Current Balance:"));
        assertTrue(statement.contains("Monthly Transaction Count:"));
        assertTrue(statement.contains("Monthly Withdrawal Count:"));
        assertTrue(statement.contains("Transaction History:"));
    }
    
    /**
     * Test toString method.
     * Verifies that toString returns properly formatted account information.
     */
    @Test
    @DisplayName("Should return formatted account information string")
    void testToString() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, initialDeposit);
        
        // Act
        String result = account.toString();
        
        // Assert
        assertTrue(result.contains("Account{"));
        assertTrue(result.contains("accountNumber='" + accountNumber + "'"));
        assertTrue(result.contains("type=CHECKING"));
        assertTrue(result.contains("customer='" + customerName + "'"));
        assertTrue(result.contains("balance=500.00"));
    }
    
    /**
     * Test account creation with large initial deposit.
     * Verifies that accounts can handle large monetary amounts.
     */
    @Test
    @DisplayName("Should create account with large initial deposit")
    void testAccountCreationWithLargeDeposit() {
        // Arrange
        double largeDeposit = 1000000.99;
        
        // Act
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, largeDeposit);
        
        // Assert
        assertEquals(largeDeposit, account.getBalance());
    }
    
    /**
     * Test boundary condition for savings minimum balance.
     * Verifies that withdrawal leaving exactly minimum balance is allowed.
     */
    @Test
    @DisplayName("Should allow savings withdrawal leaving exactly minimum balance")
    void testSavingsMinimumBalanceBoundary() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.SAVINGS, customerName, 200.0);
        double withdrawalAmount = 100.0; // Would leave exactly 100 (minimum)
        
        // Act
        Transaction transaction = account.withdraw(withdrawalAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(100.0, account.getBalance());
    }

    /**
     * Test edge case for checking account with exact transaction fee balance.
     * Verifies behavior when balance is exactly equal to transaction fee.
     */
    @Test
    @DisplayName("Should handle checking account with exact transaction fee balance")
    void testCheckingAccountTransactionFeeEdgeCase() {
        // Arrange
        Account account = new Account(accountNumber, AccountType.CHECKING, customerName, 2.50);

        // Perform 10 transactions to reach fee threshold
        for (int i = 0; i < 10; i++) {
            account.deposit(1.0);
        }

        // Act - 11th transaction should incur fee and be deducted from balance
        // Balance after 10 transactions: 2.50 + 10.00 = 12.50
        // 11th transaction: +0.01 deposit - 2.50 fee = 10.01 final balance
        Transaction transaction = account.deposit(0.01);

        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(10.01, account.getBalance(), 0.01); // Deposit amount + previous balance - transaction fee
    }
}