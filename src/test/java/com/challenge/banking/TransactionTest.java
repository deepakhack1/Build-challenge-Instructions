package com.challenge.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

/**
 * Comprehensive unit tests for the Transaction class.
 * Tests transaction creation, getters, toString, equals, and hashCode methods.
 */
class TransactionTest {
    
    private Transaction.TransactionType depositType;
    private Transaction.TransactionType withdrawalType;
    private Transaction.TransactionType transferType;
    private double amount;
    private double balanceBefore;
    private double balanceAfter;
    private String failureReason;
    
    @BeforeEach
    void setUp() {
        depositType = Transaction.TransactionType.DEPOSIT;
        withdrawalType = Transaction.TransactionType.WITHDRAWAL;
        transferType = Transaction.TransactionType.TRANSFER;
        amount = 100.0;
        balanceBefore = 500.0;
        balanceAfter = 600.0;
        failureReason = "Insufficient funds";
    }
    
    /**
     * Test successful transaction creation with all required parameters.
     * Verifies that a transaction is created with correct values and SUCCESS status.
     */
    @Test
    @DisplayName("Should create successful transaction with correct values")
    void testSuccessfulTransactionCreation() {
        // Arrange & Act
        Transaction transaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Assert
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getTimestamp());
        assertEquals(depositType, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(balanceBefore, transaction.getBalanceBefore());
        assertEquals(balanceAfter, transaction.getBalanceAfter());
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
        assertNull(transaction.getFailureReason());
    }
    
    /**
     * Test failed transaction creation with failure reason.
     * Verifies that a failed transaction maintains balance and includes failure reason.
     */
    @Test
    @DisplayName("Should create failed transaction with failure reason")
    void testFailedTransactionCreation() {
        // Arrange & Act
        Transaction transaction = new Transaction(withdrawalType, amount, balanceBefore, failureReason);
        
        // Assert
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getTimestamp());
        assertEquals(withdrawalType, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(balanceBefore, transaction.getBalanceBefore());
        assertEquals(balanceBefore, transaction.getBalanceAfter()); // Balance unchanged for failed transaction
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.getStatus());
        assertEquals(failureReason, transaction.getFailureReason());
    }
    
    /**
     * Test transaction creation with different transaction types.
     * Verifies that all transaction types can be created successfully.
     */
    @Test
    @DisplayName("Should create transactions with different types")
    void testTransactionCreationWithDifferentTypes() {
        // Arrange & Act
        Transaction depositTransaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        Transaction withdrawalTransaction = new Transaction(withdrawalType, amount, balanceAfter, balanceBefore);
        Transaction transferTransaction = new Transaction(transferType, amount, balanceBefore, balanceAfter);
        
        // Assert
        assertEquals(Transaction.TransactionType.DEPOSIT, depositTransaction.getType());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, withdrawalTransaction.getType());
        assertEquals(Transaction.TransactionType.TRANSFER, transferTransaction.getType());
    }
    
    /**
     * Test transaction ID uniqueness.
     * Verifies that each transaction gets a unique ID.
     */
    @Test
    @DisplayName("Should generate unique transaction IDs")
    void testTransactionIdUniqueness() {
        // Arrange & Act
        Transaction transaction1 = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        Transaction transaction2 = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Assert
        assertNotEquals(transaction1.getTransactionId(), transaction2.getTransactionId());
    }
    
    /**
     * Test timestamp generation.
     * Verifies that transaction timestamp is set to current time.
     */
    @Test
    @DisplayName("Should set timestamp to current time")
    void testTimestampGeneration() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now();
        
        // Act
        Transaction transaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Assert
        LocalDateTime afterCreation = LocalDateTime.now();
        assertTrue(transaction.getTimestamp().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(transaction.getTimestamp().isBefore(afterCreation.plusSeconds(1)));
    }
    
    /**
     * Test toString method for successful transaction.
     * Verifies that toString returns properly formatted string with all transaction details.
     */
    @Test
    @DisplayName("Should return formatted string for successful transaction")
    void testToStringForSuccessfulTransaction() {
        // Arrange
        Transaction transaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Act
        String result = transaction.toString();
        
        // Assert
        assertTrue(result.contains("Transaction{"));
        assertTrue(result.contains("id='" + transaction.getTransactionId() + "'"));
        assertTrue(result.contains("type=DEPOSIT"));
        assertTrue(result.contains("amount=100.00"));
        assertTrue(result.contains("balanceBefore=500.00"));
        assertTrue(result.contains("balanceAfter=600.00"));
        assertTrue(result.contains("status=SUCCESS"));
        assertFalse(result.contains("reason="));
    }
    
    /**
     * Test toString method for failed transaction.
     * Verifies that toString includes failure reason for failed transactions.
     */
    @Test
    @DisplayName("Should return formatted string with failure reason for failed transaction")
    void testToStringForFailedTransaction() {
        // Arrange
        Transaction transaction = new Transaction(withdrawalType, amount, balanceBefore, failureReason);
        
        // Act
        String result = transaction.toString();
        
        // Assert
        assertTrue(result.contains("Transaction{"));
        assertTrue(result.contains("type=WITHDRAWAL"));
        assertTrue(result.contains("status=FAILED"));
        assertTrue(result.contains("reason='" + failureReason + "'"));
    }
    
    /**
     * Test equals method for same transaction.
     * Verifies that a transaction equals itself.
     */
    @Test
    @DisplayName("Should return true when comparing transaction with itself")
    void testEqualsWithSameTransaction() {
        // Arrange
        Transaction transaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Act & Assert
        assertEquals(transaction, transaction);
    }
    
    /**
     * Test equals method for different transactions.
     * Verifies that different transactions are not equal.
     */
    @Test
    @DisplayName("Should return false when comparing different transactions")
    void testEqualsWithDifferentTransactions() {
        // Arrange
        Transaction transaction1 = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        Transaction transaction2 = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Act & Assert
        assertNotEquals(transaction1, transaction2);
    }
    
    /**
     * Test equals method with null object.
     * Verifies that transaction does not equal null.
     */
    @Test
    @DisplayName("Should return false when comparing with null")
    void testEqualsWithNull() {
        // Arrange
        Transaction transaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Act & Assert
        assertNotEquals(transaction, null);
    }
    
    /**
     * Test equals method with different object type.
     * Verifies that transaction does not equal objects of different types.
     */
    @Test
    @DisplayName("Should return false when comparing with different object type")
    void testEqualsWithDifferentObjectType() {
        // Arrange
        Transaction transaction = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        String differentObject = "Not a transaction";
        
        // Act & Assert
        assertNotEquals(transaction, differentObject);
    }
    
    /**
     * Test hashCode method consistency.
     * Verifies that hashCode is consistent with equals method.
     */
    @Test
    @DisplayName("Should return consistent hashCode based on transaction ID")
    void testHashCodeConsistency() {
        // Arrange
        Transaction transaction1 = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        Transaction transaction2 = new Transaction(depositType, amount, balanceBefore, balanceAfter);
        
        // Act & Assert
        assertEquals(transaction1.hashCode(), transaction1.hashCode()); // Same object
        assertNotEquals(transaction1.hashCode(), transaction2.hashCode()); // Different objects
    }
    
    /**
     * Test transaction with zero amount.
     * Verifies that transactions can be created with zero amount.
     */
    @Test
    @DisplayName("Should create transaction with zero amount")
    void testTransactionWithZeroAmount() {
        // Arrange & Act
        Transaction transaction = new Transaction(depositType, 0.0, balanceBefore, balanceBefore);
        
        // Assert
        assertEquals(0.0, transaction.getAmount());
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
    }
    
    /**
     * Test transaction with negative amount.
     * Verifies that transactions can be created with negative amounts (for withdrawals).
     */
    @Test
    @DisplayName("Should create transaction with negative amount")
    void testTransactionWithNegativeAmount() {
        // Arrange & Act
        Transaction transaction = new Transaction(withdrawalType, -50.0, balanceBefore, balanceBefore + 50.0);
        
        // Assert
        assertEquals(-50.0, transaction.getAmount());
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
    }
    
    /**
     * Test transaction with large amount.
     * Verifies that transactions can handle large monetary amounts.
     */
    @Test
    @DisplayName("Should create transaction with large amount")
    void testTransactionWithLargeAmount() {
        // Arrange
        double largeAmount = 1000000.99;
        
        // Act
        Transaction transaction = new Transaction(depositType, largeAmount, balanceBefore, balanceBefore + largeAmount);
        
        // Assert
        assertEquals(largeAmount, transaction.getAmount());
        assertEquals(Transaction.TransactionStatus.SUCCESS, transaction.getStatus());
    }
}