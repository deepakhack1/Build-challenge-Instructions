package com.challenge.banking;

/**
 * Enumeration of supported account types with their specific rules and characteristics.
 */
public enum AccountType {
    /**
     * Checking account: No minimum balance, $2.50 fee per transaction after 10 free transactions per month
     */
    CHECKING,
    
    /**
     * Savings account: Minimum balance of $100, earns 2% monthly interest, max 5 withdrawals per month
     */
    SAVINGS
}