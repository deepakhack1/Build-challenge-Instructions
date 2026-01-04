package com.challenge.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AccountType enumeration.
 * Tests enum values and their properties.
 */
class AccountTypeTest {
    
    /**
     * Test that AccountType enum contains expected values.
     * Verifies that all required account types are defined.
     */
    @Test
    @DisplayName("Should contain CHECKING and SAVINGS account types")
    void testAccountTypeValues() {
        // Arrange
        AccountType[] expectedTypes = {AccountType.CHECKING, AccountType.SAVINGS};
        
        // Act
        AccountType[] actualTypes = AccountType.values();
        
        // Assert
        assertEquals(2, actualTypes.length);
        assertArrayEquals(expectedTypes, actualTypes);
    }
    
    /**
     * Test AccountType.CHECKING enum value.
     * Verifies that CHECKING account type exists and has correct string representation.
     */
    @Test
    @DisplayName("Should have CHECKING account type with correct properties")
    void testCheckingAccountType() {
        // Arrange & Act
        AccountType checkingType = AccountType.CHECKING;
        
        // Assert
        assertNotNull(checkingType);
        assertEquals("CHECKING", checkingType.name());
        assertEquals("CHECKING", checkingType.toString());
        assertEquals(0, checkingType.ordinal());
    }
    
    /**
     * Test AccountType.SAVINGS enum value.
     * Verifies that SAVINGS account type exists and has correct string representation.
     */
    @Test
    @DisplayName("Should have SAVINGS account type with correct properties")
    void testSavingsAccountType() {
        // Arrange & Act
        AccountType savingsType = AccountType.SAVINGS;
        
        // Assert
        assertNotNull(savingsType);
        assertEquals("SAVINGS", savingsType.name());
        assertEquals("SAVINGS", savingsType.toString());
        assertEquals(1, savingsType.ordinal());
    }
    
    /**
     * Test AccountType.valueOf() method.
     * Verifies that enum values can be retrieved by string name.
     */
    @Test
    @DisplayName("Should retrieve account types by string name")
    void testValueOfMethod() {
        // Arrange & Act
        AccountType checkingFromString = AccountType.valueOf("CHECKING");
        AccountType savingsFromString = AccountType.valueOf("SAVINGS");
        
        // Assert
        assertEquals(AccountType.CHECKING, checkingFromString);
        assertEquals(AccountType.SAVINGS, savingsFromString);
    }
    
    /**
     * Test AccountType.valueOf() with invalid string.
     * Verifies that valueOf throws exception for invalid account type names.
     */
    @Test
    @DisplayName("Should throw exception for invalid account type name")
    void testValueOfWithInvalidString() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AccountType.valueOf("INVALID_TYPE");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            AccountType.valueOf("checking"); // Case sensitive
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            AccountType.valueOf("savings"); // Case sensitive
        });
    }
    
    /**
     * Test AccountType equality comparison.
     * Verifies that enum values can be compared correctly.
     */
    @Test
    @DisplayName("Should support equality comparison")
    void testAccountTypeEquality() {
        // Arrange
        AccountType checking1 = AccountType.CHECKING;
        AccountType checking2 = AccountType.valueOf("CHECKING");
        AccountType savings = AccountType.SAVINGS;
        
        // Act & Assert
        assertEquals(checking1, checking2);
        assertNotEquals(checking1, savings);
        assertNotEquals(checking2, savings);
        
        // Test with == operator (enums support reference equality)
        assertTrue(checking1 == checking2);
        assertFalse(checking1 == savings);
    }
    
    /**
     * Test AccountType in switch statements.
     * Verifies that enum values work correctly in switch statements.
     */
    @Test
    @DisplayName("Should work correctly in switch statements")
    void testAccountTypeInSwitch() {
        // Arrange & Act
        String checkingResult = getAccountDescription(AccountType.CHECKING);
        String savingsResult = getAccountDescription(AccountType.SAVINGS);
        
        // Assert
        assertEquals("Checking Account", checkingResult);
        assertEquals("Savings Account", savingsResult);
    }
    
    /**
     * Helper method to test enum in switch statement.
     */
    private String getAccountDescription(AccountType accountType) {
        switch (accountType) {
            case CHECKING:
                return "Checking Account";
            case SAVINGS:
                return "Savings Account";
            default:
                return "Unknown Account Type";
        }
    }
    
    /**
     * Test AccountType hashCode consistency.
     * Verifies that enum values have consistent hash codes.
     */
    @Test
    @DisplayName("Should have consistent hash codes")
    void testAccountTypeHashCode() {
        // Arrange
        AccountType checking1 = AccountType.CHECKING;
        AccountType checking2 = AccountType.valueOf("CHECKING");
        AccountType savings = AccountType.SAVINGS;
        
        // Act & Assert
        assertEquals(checking1.hashCode(), checking2.hashCode());
        assertNotEquals(checking1.hashCode(), savings.hashCode());
    }
    
    /**
     * Test AccountType with collections.
     * Verifies that enum values work correctly in collections.
     */
    @Test
    @DisplayName("Should work correctly in collections")
    void testAccountTypeInCollections() {
        // Arrange
        java.util.Set<AccountType> accountTypes = java.util.EnumSet.allOf(AccountType.class);
        
        // Act & Assert
        assertEquals(2, accountTypes.size());
        assertTrue(accountTypes.contains(AccountType.CHECKING));
        assertTrue(accountTypes.contains(AccountType.SAVINGS));
    }
    
    /**
     * Test AccountType serialization properties.
     * Verifies that enum values maintain their identity across serialization scenarios.
     */
    @Test
    @DisplayName("Should maintain identity for serialization")
    void testAccountTypeIdentity() {
        // Arrange
        AccountType original = AccountType.CHECKING;
        AccountType fromValueOf = AccountType.valueOf(original.name());
        
        // Act & Assert
        assertSame(original, fromValueOf); // Same reference
        assertEquals(original.name(), fromValueOf.name());
        assertEquals(original.ordinal(), fromValueOf.ordinal());
    }
}