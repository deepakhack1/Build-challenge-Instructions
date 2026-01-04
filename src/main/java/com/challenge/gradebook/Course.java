package com.challenge.gradebook;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a course with grading categories, weights, and assignments.
 * Manages course-level grade calculations and assignment tracking.
 */
public class Course {
    private final String courseName;
    private final int creditHours;
    private final Map<GradingCategory, Double> categoryWeights;
    private final List<Assignment> assignments;
    
    /**
     * Constructor for creating a course with default category weights
     * 
     * @param courseName Name of the course
     * @param creditHours Credit hours for the course
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Course(String courseName, int creditHours) {
        this(courseName, creditHours, getDefaultWeights());
    }
    
    /**
     * Constructor for creating a course with custom category weights
     * 
     * @param courseName Name of the course
     * @param creditHours Credit hours for the course
     * @param categoryWeights Map of category weights (must total 100%)
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Course(String courseName, int creditHours, Map<GradingCategory, Double> categoryWeights) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        if (creditHours <= 0) {
            throw new IllegalArgumentException("Credit hours must be positive");
        }
        if (categoryWeights == null || categoryWeights.isEmpty()) {
            throw new IllegalArgumentException("Category weights cannot be null or empty");
        }
        
        // Validate that weights total 100%
        double totalWeight = categoryWeights.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalWeight - 100.0) > 0.01) {
            throw new IllegalArgumentException("Category weights must total 100%, got: " + totalWeight);
        }
        
        this.courseName = courseName.trim();
        this.creditHours = creditHours;
        this.categoryWeights = new EnumMap<>(categoryWeights);
        this.assignments = new ArrayList<>();
    }
    
    /**
     * Get default category weights
     * 
     * @return Map of default category weights
     */
    private static Map<GradingCategory, Double> getDefaultWeights() {
        Map<GradingCategory, Double> weights = new EnumMap<>(GradingCategory.class);
        for (GradingCategory category : GradingCategory.values()) {
            weights.put(category, category.getDefaultWeight());
        }
        return weights;
    }
    
    /**
     * Add an assignment to the course
     * 
     * @param assignment Assignment to add
     * @throws IllegalArgumentException if assignment is null
     */
    public void addAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null");
        }
        assignments.add(assignment);
    }
    
    /**
     * Get assignments for a specific category
     * 
     * @param category Grading category
     * @return List of assignments in the category
     */
    public List<Assignment> getAssignmentsByCategory(GradingCategory category) {
        return assignments.stream()
                .filter(assignment -> assignment.getCategory() == category)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate the average score for a specific category
     * 
     * @param category Grading category
     * @return Average percentage score for the category (0-100), or 0 if no assignments
     */
    public double getCategoryAverage(GradingCategory category) {
        List<Assignment> categoryAssignments = getAssignmentsByCategory(category);
        
        if (categoryAssignments.isEmpty()) {
            return 0.0; // No assignments in category, treated as 0%
        }
        
        double totalPoints = 0.0;
        double totalPossible = 0.0;
        
        for (Assignment assignment : categoryAssignments) {
            totalPoints += assignment.getPointsEarned();
            totalPossible += assignment.getPointsPossible();
        }
        
        return totalPossible > 0 ? (totalPoints / totalPossible) * 100.0 : 0.0;
    }
    
    /**
     * Calculate the final course grade percentage
     * 
     * @return Final course grade percentage (0-100)
     */
    public double calculateFinalGrade() {
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (Map.Entry<GradingCategory, Double> entry : categoryWeights.entrySet()) {
            GradingCategory category = entry.getKey();
            double weight = entry.getValue();
            
            // Only include categories that have assignments
            if (!getAssignmentsByCategory(category).isEmpty()) {
                double categoryAverage = getCategoryAverage(category);
                weightedSum += categoryAverage * (weight / 100.0);
                totalWeight += weight;
            }
        }
        
        // If no assignments exist, return 0
        if (totalWeight == 0.0) {
            return 0.0;
        }
        
        // Normalize by actual weight used (in case some categories have no assignments)
        return (weightedSum / totalWeight) * 100.0;
    }
    
    /**
     * Get letter grade based on percentage
     * 
     * @param percentage Grade percentage
     * @return Letter grade (A, B, C, D, F)
     */
    public static String getLetterGrade(double percentage) {
        if (percentage >= 90.0) return "A";
        if (percentage >= 80.0) return "B";
        if (percentage >= 70.0) return "C";
        if (percentage >= 60.0) return "D";
        return "F";
    }
    
    /**
     * Get GPA points for a letter grade
     * 
     * @param letterGrade Letter grade (A, B, C, D, F)
     * @return GPA points (4.0, 3.0, 2.0, 1.0, 0.0)
     */
    public static double getGpaPoints(String letterGrade) {
        switch (letterGrade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: throw new IllegalArgumentException("Invalid letter grade: " + letterGrade);
        }
    }
    
    /**
     * Get final letter grade for this course
     * 
     * @return Letter grade based on final percentage
     */
    public String getFinalLetterGrade() {
        return getLetterGrade(calculateFinalGrade());
    }
    
    /**
     * Get GPA points for this course
     * 
     * @return GPA points based on final letter grade
     */
    public double getGpaPoints() {
        return getGpaPoints(getFinalLetterGrade());
    }
    
    /**
     * Generate a detailed course report
     * 
     * @return Formatted course report string
     */
    public String generateCourseReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n=== COURSE REPORT ===").append("\n");
        report.append("Course: ").append(courseName).append("\n");
        report.append("Credit Hours: ").append(creditHours).append("\n");
        report.append("Final Grade: ").append(String.format("%.1f%%", calculateFinalGrade()))
              .append(" (").append(getFinalLetterGrade()).append(")\n");
        report.append("GPA Points: ").append(String.format("%.1f", getGpaPoints())).append("\n");
        
        report.append("\nCategory Breakdown:\n");
        for (GradingCategory category : GradingCategory.values()) {
            List<Assignment> categoryAssignments = getAssignmentsByCategory(category);
            double categoryAverage = getCategoryAverage(category);
            double weight = categoryWeights.getOrDefault(category, 0.0);
            
            report.append(String.format("  %s (%.1f%%): %.1f%% [%d assignments]\n",
                    category.name(), weight, categoryAverage, categoryAssignments.size()));
        }
        
        report.append("\nAssignments:\n");
        for (Assignment assignment : assignments) {
            report.append("  ").append(assignment.toString()).append("\n");
        }
        
        return report.toString();
    }
    
    // Getters
    public String getCourseName() {
        return courseName;
    }
    
    public int getCreditHours() {
        return creditHours;
    }
    
    public Map<GradingCategory, Double> getCategoryWeights() {
        return new EnumMap<>(categoryWeights);
    }
    
    public List<Assignment> getAssignments() {
        return new ArrayList<>(assignments);
    }
    
    public int getAssignmentCount() {
        return assignments.size();
    }
    
    @Override
    public String toString() {
        return String.format("Course{name='%s', creditHours=%d, assignments=%d, finalGrade=%.1f%% (%s)}",
                courseName, creditHours, assignments.size(), calculateFinalGrade(), getFinalLetterGrade());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseName.equals(course.courseName);
    }
    
    @Override
    public int hashCode() {
        return courseName.hashCode();
    }
}