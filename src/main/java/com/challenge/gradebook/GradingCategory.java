package com.challenge.gradebook;

/**
 * Enumeration of grading categories with their default weights.
 * Each category has a specific weight that must total 100% across all categories.
 */
public enum GradingCategory {
    HOMEWORK(20.0),
    QUIZZES(20.0),
    MIDTERM(25.0),
    FINAL_EXAM(35.0);
    
    private final double defaultWeight;
    
    /**
     * Constructor for grading category with default weight
     * 
     * @param defaultWeight Default weight percentage for this category
     */
    GradingCategory(double defaultWeight) {
        this.defaultWeight = defaultWeight;
    }
    
    /**
     * Get the default weight for this category
     * 
     * @return Default weight percentage
     */
    public double getDefaultWeight() {
        return defaultWeight;
    }
    
    /**
     * Validate that all category weights total 100%
     * 
     * @return true if all default weights total 100%
     */
    public static boolean validateDefaultWeights() {
        double total = 0.0;
        for (GradingCategory category : values()) {
            total += category.getDefaultWeight();
        }
        return Math.abs(total - 100.0) < 0.01; // Allow for small floating point differences
    }
    
    @Override
    public String toString() {
        return name() + " (" + defaultWeight + "%)";
    }
}