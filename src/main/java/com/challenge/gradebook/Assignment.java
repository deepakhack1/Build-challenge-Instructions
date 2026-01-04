package com.challenge.gradebook;

/**
 * Represents an assignment with points earned, points possible, and category.
 * Immutable class for maintaining assignment data integrity.
 */
public class Assignment {
    private final String name;
    private final double pointsEarned;
    private final double pointsPossible;
    private final GradingCategory category;
    
    /**
     * Constructor for creating an assignment
     * 
     * @param name Name of the assignment
     * @param pointsEarned Points earned by the student
     * @param pointsPossible Total points possible for the assignment
     * @param category Grading category this assignment belongs to
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Assignment(String name, double pointsEarned, double pointsPossible, GradingCategory category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Assignment name cannot be null or empty");
        }
        if (pointsPossible <= 0) {
            throw new IllegalArgumentException("Points possible must be positive");
        }
        if (pointsEarned < 0 || pointsEarned > pointsPossible) {
            throw new IllegalArgumentException("Points earned must be between 0 and points possible");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        this.name = name.trim();
        this.pointsEarned = pointsEarned;
        this.pointsPossible = pointsPossible;
        this.category = category;
    }
    
    /**
     * Calculate the percentage score for this assignment
     * 
     * @return Percentage score (0-100)
     */
    public double getPercentageScore() {
        return (pointsEarned / pointsPossible) * 100.0;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public double getPointsEarned() {
        return pointsEarned;
    }
    
    public double getPointsPossible() {
        return pointsPossible;
    }
    
    public GradingCategory getCategory() {
        return category;
    }
    
    @Override
    public String toString() {
        return String.format("Assignment{name='%s', pointsEarned=%.1f, pointsPossible=%.1f, category=%s, score=%.1f%%}",
                name, pointsEarned, pointsPossible, category, getPercentageScore());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Assignment that = (Assignment) obj;
        return Double.compare(that.pointsEarned, pointsEarned) == 0 &&
               Double.compare(that.pointsPossible, pointsPossible) == 0 &&
               name.equals(that.name) &&
               category == that.category;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}