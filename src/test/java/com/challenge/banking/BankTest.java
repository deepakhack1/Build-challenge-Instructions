package com.challenge.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive unit tests for the Bank class.
 * Tests account management, transactions, transfers, and error handling.
 */
class BankTest {
    
    private Bank bank;
    private String customerName;
    private double initialDeposit;
    
    @BeforeEach
    void setUp() {
        bank = new Bank();
        customerName = "John Doe";
        initialDeposit = 500.0;
    }
    
    /**
     * Test successful checking account opening.
     * Verifies that checking account can be opened with valid parameters.
     */
    @Test
    @DisplayName("Should successfully open checking account with valid parameters")
    void testSuccessfulCheckingAccountOpening() throws BankingException {
        // Arrange & Act
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        
        // Assert
        assertNotNull(accountNumber);
        assertEquals(1, bank.getAccountCount());
        assertEquals(initialDeposit, bank.getAccountBalance(accountNumber));
        
        Account account = bank.getAccountInfo(accountNumber);
        assertEquals(customerName, account.getCustomerName());
        assertEquals(AccountType.CHECKING, account.getAccountType());
    }
    
    /**
     * Test successful savings account opening.
     * Verifies that savings account can be opened with valid parameters.
     */
    @Test
    @DisplayName("Should successfully open savings account with valid parameters")
    void testSuccessfulSavingsAccountOpening() throws BankingException {
        // Arrange
        double savingsDeposit = 150.0; // Above minimum requirement
        
        // Act
        String accountNumber = bank.openAccount(customerName, AccountType.SAVINGS, savingsDeposit);
        
        // Assert
        assertNotNull(accountNumber);
        assertEquals(1, bank.getAccountCount());
        assertEquals(savingsDeposit, bank.getAccountBalance(accountNumber));
        
        Account account = bank.getAccountInfo(accountNumber);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
    }
    
    /**
     * Test account opening with different initial deposits.
     * Verifies that accounts can be opened with various deposit amounts.
     */
    @Test
    @DisplayName("Should open accounts with different initial deposits")
    void testAccountOpeningWithDifferentDeposits() throws BankingException {
        // Arrange & Act
        String account1 = bank.openAccount("Customer 1", AccountType.CHECKING, 0.0);
        String account2 = bank.openAccount("Customer 2", AccountType.CHECKING, 1000.0);
        String account3 = bank.openAccount("Customer 3", AccountType.SAVINGS, 100.0);
        
        // Assert
        assertEquals(3, bank.getAccountCount());
        assertEquals(0.0, bank.getAccountBalance(account1));
        assertEquals(1000.0, bank.getAccountBalance(account2));
        assertEquals(100.0, bank.getAccountBalance(account3));
    }
    
    /**
     * Test failed account opening with empty customer name.
     * Verifies that account opening is rejected for empty customer name.
     */
    @Test
    @DisplayName("Should reject account opening with empty customer name")
    void testFailedAccountOpeningEmptyCustomerName() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.openAccount("", AccountType.CHECKING, initialDeposit);
        });
        assertEquals("Customer name cannot be empty", exception.getMessage());
        assertEquals(0, bank.getAccountCount());
    }
    
    /**
     * Test failed account opening with null customer name.
     * Verifies that account opening is rejected for null customer name.
     */
    @Test
    @DisplayName("Should reject account opening with null customer name")
    void testFailedAccountOpeningNullCustomerName() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.openAccount(null, AccountType.CHECKING, initialDeposit);
        });
        assertEquals("Customer name cannot be empty", exception.getMessage());
    }
    
    /**
     * Test failed account opening with negative initial deposit.
     * Verifies that account opening is rejected for negative deposit.
     */
    @Test
    @DisplayName("Should reject account opening with negative initial deposit")
    void testFailedAccountOpeningNegativeDeposit() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.openAccount(customerName, AccountType.CHECKING, -100.0);
        });
        assertEquals("Initial deposit cannot be negative", exception.getMessage());
    }
    
    /**
     * Test failed savings account opening with insufficient initial deposit.
     * Verifies that savings account opening is rejected for deposits below minimum.
     */
    @Test
    @DisplayName("Should reject savings account opening with insufficient initial deposit")
    void testFailedSavingsAccountOpeningInsufficientDeposit() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.openAccount(customerName, AccountType.SAVINGS, 50.0);
        });
        assertTrue(exception.getMessage().contains("minimum initial deposit of $100.00"));
    }
    
    /**
     * Test successful account closing with zero balance.
     * Verifies that account can be closed when balance is zero.
     */
    @Test
    @DisplayName("Should successfully close account with zero balance")
    void testSuccessfulAccountClosing() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, 100.0);
        bank.withdraw(accountNumber, 100.0); // Withdraw all funds
        
        // Act
        bank.closeAccount(accountNumber);
        
        // Assert
        assertEquals(0, bank.getAccountCount());
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.getAccountInfo(accountNumber);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
    }
    
    /**
     * Test failed account closing with non-zero balance.
     * Verifies that account closing is rejected when balance is not zero.
     */
    @Test
    @DisplayName("Should reject account closing with non-zero balance")
    void testFailedAccountClosingNonZeroBalance() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, 100.0);
        
        // Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.closeAccount(accountNumber);
        });
        assertTrue(exception.getMessage().contains("Cannot close account with non-zero balance"));
        assertEquals(1, bank.getAccountCount());
    }
    
    /**
     * Test failed account closing for non-existent account.
     * Verifies that closing non-existent account throws appropriate exception.
     */
    @Test
    @DisplayName("Should reject closing non-existent account")
    void testFailedAccountClosingNonExistent() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.closeAccount("999999");
        });
        assertTrue(exception.getMessage().contains("Account not found"));
    }
    
    /**
     * Test successful deposit operation through bank.
     * Verifies that deposit through bank updates account balance correctly.
     */
    @Test
    @DisplayName("Should successfully process deposit through bank")
    void testSuccessfulBankDeposit() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        double depositAmount = 100.0;
        
        // Act
        Transaction transaction = bank.deposit(accountNumber, depositAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(Transaction.TransactionType.DEPOSIT, transaction.getType());
        assertEquals(depositAmount, transaction.getAmount());
        assertEquals(initialDeposit + depositAmount, bank.getAccountBalance(accountNumber));
    }
    
    /**
     * Test deposit to non-existent account.
     * Verifies that deposit to non-existent account throws appropriate exception.
     */
    @Test
    @DisplayName("Should reject deposit to non-existent account")
    void testDepositToNonExistentAccount() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.deposit("999999", 100.0);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
    }
    
    /**
     * Test successful withdrawal operation through bank.
     * Verifies that withdrawal through bank updates account balance correctly.
     */
    @Test
    @DisplayName("Should successfully process withdrawal through bank")
    void testSuccessfulBankWithdrawal() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        double withdrawalAmount = 100.0;
        
        // Act
        Transaction transaction = bank.withdraw(accountNumber, withdrawalAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals(withdrawalAmount, transaction.getAmount());
        assertEquals(initialDeposit - withdrawalAmount, bank.getAccountBalance(accountNumber));
    }
    
    /**
     * Test withdrawal from non-existent account.
     * Verifies that withdrawal from non-existent account throws appropriate exception.
     */
    @Test
    @DisplayName("Should reject withdrawal from non-existent account")
    void testWithdrawalFromNonExistentAccount() {
        // Arrange & Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.withdraw("999999", 100.0);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
    }
    
    /**
     * Test successful transfer between accounts.
     * Verifies that transfer correctly moves money between two accounts.
     */
    @Test
    @DisplayName("Should successfully transfer money between accounts")
    void testSuccessfulTransfer() throws BankingException {
        // Arrange
        String fromAccount = bank.openAccount("Customer 1", AccountType.CHECKING, 500.0);
        String toAccount = bank.openAccount("Customer 2", AccountType.CHECKING, 200.0);
        double transferAmount = 150.0;
        
        // Act
        Transaction[] transactions = bank.transfer(fromAccount, toAccount, transferAmount);
        
        // Assert
        assertEquals(2, transactions.length);
        assertEquals(Transaction.TransactionStatus.SUCCESS, transactions[0].getStatus());
        assertEquals(Transaction.TransactionStatus.SUCCESS, transactions[1].getStatus());
        assertEquals(Transaction.TransactionType.TRANSFER, transactions[0].getType());
        assertEquals(Transaction.TransactionType.TRANSFER, transactions[1].getType());
        
        assertEquals(350.0, bank.getAccountBalance(fromAccount)); // 500 - 150
        assertEquals(350.0, bank.getAccountBalance(toAccount)); // 200 + 150
    }
    
    /**
     * Test failed transfer with insufficient funds.
     * Verifies that transfer fails when source account has insufficient funds.
     */
    @Test
    @DisplayName("Should fail transfer with insufficient funds")
    void testFailedTransferInsufficientFunds() throws BankingException {
        // Arrange
        String fromAccount = bank.openAccount("Customer 1", AccountType.CHECKING, 100.0);
        String toAccount = bank.openAccount("Customer 2", AccountType.CHECKING, 200.0);
        double transferAmount = 150.0; // More than available
        
        // Act
        Transaction[] transactions = bank.transfer(fromAccount, toAccount, transferAmount);
        
        // Assert
        assertEquals(2, transactions.length);
        assertEquals(Transaction.TransactionStatus.FAILED, transactions[0].getStatus());
        assertEquals(Transaction.TransactionStatus.FAILED, transactions[1].getStatus());
        
        // Balances should remain unchanged
        assertEquals(100.0, bank.getAccountBalance(fromAccount));
        assertEquals(200.0, bank.getAccountBalance(toAccount));
    }
    
    /**
     * Test failed transfer to same account.
     * Verifies that transfer to the same account is rejected.
     */
    @Test
    @DisplayName("Should reject transfer to same account")
    void testFailedTransferToSameAccount() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        
        // Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.transfer(accountNumber, accountNumber, 100.0);
        });
        assertEquals("Cannot transfer to the same account", exception.getMessage());
    }
    
    /**
     * Test transfer with non-existent source account.
     * Verifies that transfer fails when source account doesn't exist.
     */
    @Test
    @DisplayName("Should reject transfer from non-existent source account")
    void testTransferFromNonExistentAccount() throws BankingException {
        // Arrange
        String toAccount = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        
        // Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.transfer("999999", toAccount, 100.0);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
    }
    
    /**
     * Test transfer with non-existent destination account.
     * Verifies that transfer fails when destination account doesn't exist.
     */
    @Test
    @DisplayName("Should reject transfer to non-existent destination account")
    void testTransferToNonExistentAccount() throws BankingException {
        // Arrange
        String fromAccount = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        
        // Act & Assert
        BankingException exception = assertThrows(BankingException.class, () -> {
            bank.transfer(fromAccount, "999999", 100.0);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
    }
    
    /**
     * Test transaction history retrieval through bank.
     * Verifies that transaction history can be retrieved through bank interface.
     */
    @Test
    @DisplayName("Should retrieve transaction history through bank")
    void testTransactionHistoryRetrieval() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        bank.deposit(accountNumber, 100.0);
        bank.withdraw(accountNumber, 50.0);
        
        // Act
        List<Transaction> history = bank.getTransactionHistory(accountNumber);
        
        // Assert
        assertEquals(3, history.size()); // Initial deposit + deposit + withdrawal
        assertEquals(Transaction.TransactionType.DEPOSIT, history.get(0).getType());
        assertEquals(Transaction.TransactionType.DEPOSIT, history.get(1).getType());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, history.get(2).getType());
    }
    
    /**
     * Test transaction history retrieval with date range.
     * Verifies that transaction history can be filtered by date range through bank.
     */
    @Test
    @DisplayName("Should retrieve transaction history with date range")
    void testTransactionHistoryWithDateRange() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.CHECKING, initialDeposit);
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        
        bank.deposit(accountNumber, 100.0);
        bank.withdraw(accountNumber, 50.0);
        
        // Act
        List<Transaction> history = bank.getTransactionHistory(accountNumber, startDate, endDate);
        
        // Assert
        assertEquals(3, history.size()); // All transactions should be within range
    }
    
    /**
     * Test monthly interest application to all savings accounts.
     * Verifies that monthly interest is applied only to savings accounts.
     */
    @Test
    @DisplayName("Should apply monthly interest to all savings accounts")
    void testMonthlyInterestApplication() throws BankingException {
        // Arrange
        String checkingAccount = bank.openAccount("Customer 1", AccountType.CHECKING, 1000.0);
        String savingsAccount1 = bank.openAccount("Customer 2", AccountType.SAVINGS, 1000.0);
        String savingsAccount2 = bank.openAccount("Customer 3", AccountType.SAVINGS, 500.0);
        
        // Act
        bank.applyMonthlyInterest();
        
        // Assert
        assertEquals(1000.0, bank.getAccountBalance(checkingAccount)); // No interest
        assertEquals(1020.0, bank.getAccountBalance(savingsAccount1), 0.01); // 2% interest
        assertEquals(510.0, bank.getAccountBalance(savingsAccount2), 0.01); // 2% interest
    }
    
    /**
     * Test monthly statement generation through bank.
     * Verifies that monthly statement can be generated through bank interface.
     */
    @Test
    @DisplayName("Should generate monthly statement through bank")
    void testMonthlyStatementGeneration() throws BankingException {
        // Arrange
        String accountNumber = bank.openAccount(customerName, AccountType.SAVINGS, initialDeposit);
        bank.deposit(accountNumber, 100.0);
        
        // Act
        String statement = bank.generateMonthlyStatement(accountNumber);
        
        // Assert
        assertTrue(statement.contains("MONTHLY STATEMENT"));
        assertTrue(statement.contains(accountNumber));
        assertTrue(statement.contains(customerName));
        assertTrue(statement.contains("SAVINGS"));
    }
    
    /**
     * Test getting all accounts for administrative purposes.
     * Verifies that all accounts can be retrieved for administrative operations.
     */
    @Test
    @DisplayName("Should retrieve all accounts for administration")
    void testGetAllAccounts() throws BankingException {
        // Arrange
        String account1 = bank.openAccount("Customer 1", AccountType.CHECKING, 500.0);
        String account2 = bank.openAccount("Customer 2", AccountType.SAVINGS, 1000.0);
        String account3 = bank.openAccount("Customer 3", AccountType.CHECKING, 200.0);
        
        // Act
        Map<String, Account> allAccounts = bank.getAllAccounts();
        
        // Assert
        assertEquals(3, allAccounts.size());
        assertTrue(allAccounts.containsKey(account1));
        assertTrue(allAccounts.containsKey(account2));
        assertTrue(allAccounts.containsKey(account3));
    }
    
    /**
     * Test account number generation uniqueness.
     * Verifies that each new account gets a unique account number.
     */
    @Test
    @DisplayName("Should generate unique account numbers")
    void testUniqueAccountNumberGeneration() throws BankingException {
        // Arrange & Act
        String account1 = bank.openAccount("Customer 1", AccountType.CHECKING, 100.0);
        String account2 = bank.openAccount("Customer 2", AccountType.CHECKING, 100.0);
        String account3 = bank.openAccount("Customer 3", AccountType.CHECKING, 100.0);
        
        // Assert
        assertNotEquals(account1, account2);
        assertNotEquals(account2, account3);
        assertNotEquals(account1, account3);
    }
    
    /**
     * Test error handling for operations on non-existent accounts.
     * Verifies that all bank operations properly handle non-existent accounts.
     */
    @Test
    @DisplayName("Should handle operations on non-existent accounts gracefully")
    void testErrorHandlingForNonExistentAccounts() {
        // Arrange
        String nonExistentAccount = "999999";
        
        // Act & Assert
        assertThrows(BankingException.class, () -> bank.getAccountBalance(nonExistentAccount));
        assertThrows(BankingException.class, () -> bank.getAccountInfo(nonExistentAccount));
        assertThrows(BankingException.class, () -> bank.deposit(nonExistentAccount, 100.0));
        assertThrows(BankingException.class, () -> bank.withdraw(nonExistentAccount, 100.0));
        assertThrows(BankingException.class, () -> bank.getTransactionHistory(nonExistentAccount));
        assertThrows(BankingException.class, () -> bank.generateMonthlyStatement(nonExistentAccount));
        assertThrows(BankingException.class, () -> bank.closeAccount(nonExistentAccount));
    }
    
    /**
     * Test boundary condition for savings account minimum initial deposit.
     * Verifies that savings account can be opened with exactly minimum deposit.
     */
    @Test
    @DisplayName("Should allow savings account with exactly minimum initial deposit")
    void testSavingsAccountMinimumDepositBoundary() throws BankingException {
        // Arrange & Act
        String accountNumber = bank.openAccount(customerName, AccountType.SAVINGS, 100.0);
        
        // Assert
        assertNotNull(accountNumber);
        assertEquals(100.0, bank.getAccountBalance(accountNumber));
    }
    
    /**
     * Test large transfer operation.
     * Verifies that bank can handle large monetary transfers.
     */
    @Test
    @DisplayName("Should handle large transfer amounts")
    void testLargeTransferOperation() throws BankingException {
        // Arrange
        double largeAmount = 1000000.0;
        String fromAccount = bank.openAccount("Rich Customer", AccountType.CHECKING, largeAmount + 1000.0);
        String toAccount = bank.openAccount("Recipient", AccountType.CHECKING, 0.0);
        
        // Act
        Transaction[] transactions = bank.transfer(fromAccount, toAccount, largeAmount);
        
        // Assert
        assertEquals(Transaction.TransactionStatus.SUCCESS, transactions[0].getStatus());
        assertEquals(Transaction.TransactionStatus.SUCCESS, transactions[1].getStatus());
        assertEquals(1000.0, bank.getAccountBalance(fromAccount));
        assertEquals(largeAmount, bank.getAccountBalance(toAccount));
    }
}