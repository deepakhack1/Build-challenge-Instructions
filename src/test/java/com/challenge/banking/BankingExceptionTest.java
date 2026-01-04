package com.challenge.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BankingException class.
 * Tests exception creation, message handling, and cause chaining.
 */
class BankingExceptionTest {
    
    /**
     * Test BankingException creation with message only.
     * Verifies that exception can be created with just a message.
     */
    @Test
    @DisplayName("Should create BankingException with message only")
    void testBankingExceptionWithMessage() {
        // Arrange
        String errorMessage = "Account not found";
        
        // Act
        BankingException exception = new BankingException(errorMessage);
        
        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof Exception);
    }
    
    /**
     * Test BankingException creation with message and cause.
     * Verifies that exception can be created with message and underlying cause.
     */
    @Test
    @DisplayName("Should create BankingException with message and cause")
    void testBankingExceptionWithMessageAndCause() {
        // Arrange
        String errorMessage = "Database connection failed";
        RuntimeException cause = new RuntimeException("Connection timeout");
        
        // Act
        BankingException exception = new BankingException(errorMessage, cause);
        
        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Connection timeout", exception.getCause().getMessage());
    }
    
    /**
     * Test BankingException with null message.
     * Verifies that exception handles null message gracefully.
     */
    @Test
    @DisplayName("Should handle null message gracefully")
    void testBankingExceptionWithNullMessage() {
        // Arrange & Act
        BankingException exception = new BankingException(null);
        
        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }
    
    /**
     * Test BankingException with empty message.
     * Verifies that exception can be created with empty message.
     */
    @Test
    @DisplayName("Should create BankingException with empty message")
    void testBankingExceptionWithEmptyMessage() {
        // Arrange
        String emptyMessage = "";
        
        // Act
        BankingException exception = new BankingException(emptyMessage);
        
        // Assert
        assertEquals(emptyMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    /**
     * Test BankingException with null cause.
     * Verifies that exception handles null cause gracefully.
     */
    @Test
    @DisplayName("Should handle null cause gracefully")
    void testBankingExceptionWithNullCause() {
        // Arrange
        String errorMessage = "Operation failed";
        
        // Act
        BankingException exception = new BankingException(errorMessage, null);
        
        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    /**
     * Test BankingException inheritance.
     * Verifies that BankingException properly extends Exception.
     */
    @Test
    @DisplayName("Should properly extend Exception class")
    void testBankingExceptionInheritance() {
        // Arrange
        BankingException exception = new BankingException("Test message");
        
        // Act & Assert
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }
    
    /**
     * Test BankingException can be thrown and caught.
     * Verifies that exception can be properly thrown and caught in try-catch blocks.
     */
    @Test
    @DisplayName("Should be throwable and catchable")
    void testBankingExceptionThrowAndCatch() {
        // Arrange
        String expectedMessage = "Insufficient funds";
        
        // Act & Assert
        BankingException caughtException = assertThrows(BankingException.class, () -> {
            throw new BankingException(expectedMessage);
        });
        
        assertEquals(expectedMessage, caughtException.getMessage());
    }
    
    /**
     * Test BankingException with different exception types as cause.
     * Verifies that various exception types can be used as causes.
     */
    @Test
    @DisplayName("Should accept different exception types as cause")
    void testBankingExceptionWithDifferentCauseTypes() {
        // Arrange
        String message = "Operation failed";
        
        // Test with RuntimeException
        RuntimeException runtimeCause = new RuntimeException("Runtime error");
        BankingException exception1 = new BankingException(message, runtimeCause);
        
        // Test with IllegalArgumentException
        IllegalArgumentException illegalArgCause = new IllegalArgumentException("Invalid argument");
        BankingException exception2 = new BankingException(message, illegalArgCause);
        
        // Test with another BankingException
        BankingException bankingCause = new BankingException("Original banking error");
        BankingException exception3 = new BankingException(message, bankingCause);
        
        // Assert
        assertEquals(runtimeCause, exception1.getCause());
        assertEquals(illegalArgCause, exception2.getCause());
        assertEquals(bankingCause, exception3.getCause());
    }

    /**
     * Test BankingException stack trace functionality.
     * Verifies that exception maintains proper stack trace information.
     */
    @Test
    @DisplayName("Should maintain proper stack trace")
    void testBankingExceptionStackTrace() {
        // Arrange & Act
        BankingException exception = new BankingException("Test exception");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        // Assert
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
        assertEquals("testBankingExceptionStackTrace", stackTrace[0].getMethodName());
        assertEquals("com.challenge.banking.BankingExceptionTest", stackTrace[0].getClassName());
    }
    
    /**
     * Test BankingException toString method.
     * Verifies that exception provides meaningful string representation.
     */
    @Test
    @DisplayName("Should provide meaningful toString representation")
    void testBankingExceptionToString() {
        // Arrange
        String message = "Account validation failed";
        BankingException exception = new BankingException(message);
        
        // Act
        String stringRepresentation = exception.toString();
        
        // Assert
        assertNotNull(stringRepresentation);
        assertTrue(stringRepresentation.contains("BankingException"));
        assertTrue(stringRepresentation.contains(message));
    }
    
    /**
     * Test BankingException with very long message.
     * Verifies that exception can handle long error messages.
     */
    @Test
    @DisplayName("Should handle very long error messages")
    void testBankingExceptionWithLongMessage() {
        // Arrange
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longMessage.append("This is a very long error message that exceeds normal length. ");
        }
        String message = longMessage.toString();
        
        // Act
        BankingException exception = new BankingException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
        assertTrue(exception.getMessage().length() > 5000);
    }
    
    /**
     * Test BankingException with special characters in message.
     * Verifies that exception handles special characters and unicode properly.
     */
    @Test
    @DisplayName("Should handle special characters in message")
    void testBankingExceptionWithSpecialCharacters() {
        // Arrange
        String messageWithSpecialChars = "Account error: ñáéíóú $€£¥ 中文 🏦💰";
        
        // Act
        BankingException exception = new BankingException(messageWithSpecialChars);
        
        // Assert
        assertEquals(messageWithSpecialChars, exception.getMessage());
    }
    
    /**
     * Test BankingException in method signature.
     * Verifies that BankingException can be properly declared in method signatures.
     */
    @Test
    @DisplayName("Should work properly in method signatures")
    void testBankingExceptionInMethodSignature() {
        // Act & Assert
        assertThrows(BankingException.class, () -> {
            methodThatThrowsBankingException();
        });
        
        assertDoesNotThrow(() -> {
            methodThatMightThrowBankingException(false);
        });
    }
    
    /**
     * Helper method that throws BankingException for testing.
     */
    private void methodThatThrowsBankingException() throws BankingException {
        throw new BankingException("Method threw banking exception");
    }
    
    /**
     * Helper method that conditionally throws BankingException for testing.
     */
    private void methodThatMightThrowBankingException(boolean shouldThrow) throws BankingException {
        if (shouldThrow) {
            throw new BankingException("Conditional exception");
        }
    }
    
    /**
     * Test BankingException cause chain.
     * Verifies that exception cause chain is properly maintained.
     */
    @Test
    @DisplayName("Should maintain proper cause chain")
    void testBankingExceptionCauseChain() {
        // Arrange
        RuntimeException rootCause = new RuntimeException("Root cause");
        IllegalStateException intermediateCause = new IllegalStateException("Intermediate cause", rootCause);
        BankingException topException = new BankingException("Top level exception", intermediateCause);
        
        // Act & Assert
        assertEquals(intermediateCause, topException.getCause());
        assertEquals(rootCause, topException.getCause().getCause());
        assertNull(topException.getCause().getCause().getCause());
        
        // Test cause chain traversal
        Throwable current = topException;
        int chainLength = 0;
        while (current != null) {
            chainLength++;
            current = current.getCause();
        }
        assertEquals(3, chainLength);
    }
}