package com.challenge.gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Assignment class.
 * Tests assignment creation, validation, calculations, and object methods.
 */
@DisplayName("Assignment Class Tests")
class AssignmentTest {

    private Assignment validAssignment;
    private static final String VALID_NAME = "Homework 1";
    private static final double VALID_POINTS_EARNED = 85.0;
    private static final double VALID_POINTS_POSSIBLE = 100.0;
    private static final GradingCategory VALID_CATEGORY = GradingCategory.HOMEWORK;

    @BeforeEach
    void setUp() {
        validAssignment = new Assignment(VALID_NAME, VALID_POINTS_EARNED, VALID_POINTS_POSSIBLE, VALID_CATEGORY);
    }

    // Assignment Creation Tests

    /**
     * Test successful assignment creation with valid parameters.
     * Verifies that all properties are correctly set.
     */
    @Test
    @DisplayName("Should create assignment with valid parameters")
    void shouldCreateAssignmentWithValidParameters() {
        // Act & Assert
        assertThat(validAssignment.getName()).isEqualTo(VALID_NAME);
        assertThat(validAssignment.getPointsEarned()).isEqualTo(VALID_POINTS_EARNED);
        assertThat(validAssignment.getPointsPossible()).isEqualTo(VALID_POINTS_POSSIBLE);
        assertThat(validAssignment.getCategory()).isEqualTo(VALID_CATEGORY);
    }

    /**
     * Test assignment creation with whitespace in name.
     * Verifies that leading and trailing whitespace is trimmed.
     */
    @Test
    @DisplayName("Should trim whitespace from assignment name")
    void shouldTrimWhitespaceFromAssignmentName() {
        // Arrange
        String nameWithWhitespace = "  Quiz 1  ";
        
        // Act
        Assignment assignment = new Assignment(nameWithWhitespace, 90.0, 100.0, GradingCategory.QUIZZES);
        
        // Assert
        assertThat(assignment.getName()).isEqualTo("Quiz 1");
    }

    /**
     * Test assignment creation with perfect score.
     * Verifies that points earned can equal points possible.
     */
    @Test
    @DisplayName("Should create assignment with perfect score")
    void shouldCreateAssignmentWithPerfectScore() {
        // Act
        Assignment perfectAssignment = new Assignment("Perfect Test", 100.0, 100.0, GradingCategory.MIDTERM);
        
        // Assert
        assertThat(perfectAssignment.getPercentageScore()).isEqualTo(100.0);
    }

    /**
     * Test assignment creation with zero points earned.
     * Verifies that zero points earned is valid.
     */
    @Test
    @DisplayName("Should create assignment with zero points earned")
    void shouldCreateAssignmentWithZeroPointsEarned() {
        // Act
        Assignment zeroAssignment = new Assignment("Failed Test", 0.0, 100.0, GradingCategory.FINAL_EXAM);
        
        // Assert
        assertThat(zeroAssignment.getPercentageScore()).isEqualTo(0.0);
    }

    // Invalid Parameter Tests

    /**
     * Test assignment creation with null name.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for null name")
    void shouldThrowExceptionForNullName() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment(null, 85.0, 100.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assignment name cannot be null or empty");
    }

    /**
     * Test assignment creation with empty name.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for empty name")
    void shouldThrowExceptionForEmptyName() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("", 85.0, 100.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assignment name cannot be null or empty");
    }

    /**
     * Test assignment creation with whitespace-only name.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for whitespace-only name")
    void shouldThrowExceptionForWhitespaceOnlyName() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("   ", 85.0, 100.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assignment name cannot be null or empty");
    }

    /**
     * Test assignment creation with zero points possible.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for zero points possible")
    void shouldThrowExceptionForZeroPointsPossible() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("Test", 0.0, 0.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Points possible must be positive");
    }

    /**
     * Test assignment creation with negative points possible.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for negative points possible")
    void shouldThrowExceptionForNegativePointsPossible() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("Test", 50.0, -100.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Points possible must be positive");
    }

    /**
     * Test assignment creation with negative points earned.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for negative points earned")
    void shouldThrowExceptionForNegativePointsEarned() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("Test", -10.0, 100.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Points earned must be between 0 and points possible");
    }

    /**
     * Test assignment creation with points earned exceeding points possible.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception when points earned exceeds points possible")
    void shouldThrowExceptionWhenPointsEarnedExceedsPointsPossible() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("Test", 110.0, 100.0, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Points earned must be between 0 and points possible");
    }

    /**
     * Test assignment creation with null category.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for null category")
    void shouldThrowExceptionForNullCategory() {
        // Act & Assert
        assertThatThrownBy(() -> new Assignment("Test", 85.0, 100.0, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category cannot be null");
    }

    // Percentage Score Calculation Tests

    /**
     * Test percentage score calculation with various valid scores.
     * Verifies accurate percentage calculations.
     */
    @ParameterizedTest
    @CsvSource({
            "85.0, 100.0, 85.0",
            "90.0, 100.0, 90.0",
            "50.0, 100.0, 50.0",
            "75.0, 80.0, 93.75",
            "0.0, 100.0, 0.0",
            "100.0, 100.0, 100.0",
            "45.0, 50.0, 90.0"
    })
    @DisplayName("Should calculate correct percentage scores")
    void shouldCalculateCorrectPercentageScores(double pointsEarned, double pointsPossible, double expectedPercentage) {
        // Arrange
        Assignment assignment = new Assignment("Test", pointsEarned, pointsPossible, GradingCategory.HOMEWORK);
        
        // Act
        double actualPercentage = assignment.getPercentageScore();
        
        // Assert
        assertThat(actualPercentage).isCloseTo(expectedPercentage, within(0.01));
    }

    /**
     * Test percentage score calculation with decimal points.
     * Verifies precision in percentage calculations.
     */
    @Test
    @DisplayName("Should handle decimal precision in percentage calculation")
    void shouldHandleDecimalPrecisionInPercentageCalculation() {
        // Arrange
        Assignment assignment = new Assignment("Decimal Test", 87.5, 125.0, GradingCategory.QUIZZES);
        
        // Act
        double percentage = assignment.getPercentageScore();
        
        // Assert
        assertThat(percentage).isCloseTo(70.0, within(0.01));
    }

    // Getter Tests

    /**
     * Test all getter methods return correct values.
     * Verifies that getters return the values set during construction.
     */
    @Test
    @DisplayName("Should return correct values from getters")
    void shouldReturnCorrectValuesFromGetters() {
        // Act & Assert
        assertThat(validAssignment.getName()).isEqualTo(VALID_NAME);
        assertThat(validAssignment.getPointsEarned()).isEqualTo(VALID_POINTS_EARNED);
        assertThat(validAssignment.getPointsPossible()).isEqualTo(VALID_POINTS_POSSIBLE);
        assertThat(validAssignment.getCategory()).isEqualTo(VALID_CATEGORY);
    }

    // Equals and HashCode Tests

    /**
     * Test equals method with identical assignments.
     * Verifies that assignments with same values are equal.
     */
    @Test
    @DisplayName("Should be equal when all properties match")
    void shouldBeEqualWhenAllPropertiesMatch() {
        // Arrange
        Assignment assignment1 = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThat(assignment1).isEqualTo(assignment2);
        assertThat(assignment1.hashCode()).isEqualTo(assignment2.hashCode());
    }

    /**
     * Test equals method with different names.
     * Verifies that assignments with different names are not equal.
     */
    @Test
    @DisplayName("Should not be equal when names differ")
    void shouldNotBeEqualWhenNamesDiffer() {
        // Arrange
        Assignment assignment1 = new Assignment("Test 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Test 2", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    /**
     * Test equals method with different points earned.
     * Verifies that assignments with different points earned are not equal.
     */
    @Test
    @DisplayName("Should not be equal when points earned differ")
    void shouldNotBeEqualWhenPointsEarnedDiffer() {
        // Arrange
        Assignment assignment1 = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Test", 90.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    /**
     * Test equals method with different points possible.
     * Verifies that assignments with different points possible are not equal.
     */
    @Test
    @DisplayName("Should not be equal when points possible differ")
    void shouldNotBeEqualWhenPointsPossibleDiffer() {
        // Arrange
        Assignment assignment1 = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Test", 85.0, 120.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    /**
     * Test equals method with different categories.
     * Verifies that assignments with different categories are not equal.
     */
    @Test
    @DisplayName("Should not be equal when categories differ")
    void shouldNotBeEqualWhenCategoriesDiffer() {
        // Arrange
        Assignment assignment1 = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Test", 85.0, 100.0, GradingCategory.QUIZZES);
        
        // Act & Assert
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    /**
     * Test equals method with null object.
     * Verifies that assignment is not equal to null.
     */
    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Act & Assert
        assertThat(validAssignment).isNotEqualTo(null);
    }

    /**
     * Test equals method with different class object.
     * Verifies that assignment is not equal to objects of different classes.
     */
    @Test
    @DisplayName("Should not be equal to different class object")
    void shouldNotBeEqualToDifferentClassObject() {
        // Act & Assert
        assertThat(validAssignment).isNotEqualTo("Not an Assignment");
    }

    /**
     * Test equals method reflexivity.
     * Verifies that an assignment equals itself.
     */
    @Test
    @DisplayName("Should be equal to itself (reflexivity)")
    void shouldBeEqualToItself() {
        // Act & Assert
        assertThat(validAssignment).isEqualTo(validAssignment);
    }

    /**
     * Test hashCode consistency.
     * Verifies that hashCode is consistent across multiple calls.
     */
    @Test
    @DisplayName("Should have consistent hashCode")
    void shouldHaveConsistentHashCode() {
        // Act
        int hashCode1 = validAssignment.hashCode();
        int hashCode2 = validAssignment.hashCode();
        
        // Assert
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    /**
     * Test hashCode based on name.
     * Verifies that hashCode is based on assignment name.
     */
    @Test
    @DisplayName("Should have hashCode based on name")
    void shouldHaveHashCodeBasedOnName() {
        // Arrange
        Assignment assignment1 = new Assignment("Same Name", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Same Name", 90.0, 120.0, GradingCategory.QUIZZES);
        
        // Act & Assert
        assertThat(assignment1.hashCode()).isEqualTo(assignment2.hashCode());
    }

    // ToString Tests

    /**
     * Test toString method format and content.
     * Verifies that toString returns properly formatted string with all information.
     */
    @Test
    @DisplayName("Should return properly formatted toString")
    void shouldReturnProperlyFormattedToString() {
        // Act
        String result = validAssignment.toString();
        
        // Assert
        assertThat(result)
                .contains("Assignment{")
                .contains("name='" + VALID_NAME + "'")
                .contains("pointsEarned=" + VALID_POINTS_EARNED)
                .contains("pointsPossible=" + VALID_POINTS_POSSIBLE)
                .contains("category=" + VALID_CATEGORY)
                .contains("score=85.0%");
    }

    /**
     * Test toString method with different assignment values.
     * Verifies that toString correctly displays different assignment data.
     */
    @Test
    @DisplayName("Should display correct values in toString")
    void shouldDisplayCorrectValuesInToString() {
        // Arrange
        Assignment assignment = new Assignment("Final Exam", 92.5, 100.0, GradingCategory.FINAL_EXAM);
        
        // Act
        String result = assignment.toString();
        
        // Assert
        assertThat(result)
                .contains("name='Final Exam'")
                .contains("pointsEarned=92.5")
                .contains("pointsPossible=100.0")
                .contains("category=FINAL_EXAM")
                .contains("score=92.5%");
    }

    // Edge Cases and Boundary Conditions

    /**
     * Test assignment with very small point values.
     * Verifies that small decimal values are handled correctly.
     */
    @Test
    @DisplayName("Should handle very small point values")
    void shouldHandleVerySmallPointValues() {
        // Arrange & Act
        Assignment assignment = new Assignment("Small Points", 0.1, 0.5, GradingCategory.HOMEWORK);
        
        // Assert
        assertThat(assignment.getPercentageScore()).isCloseTo(20.0, within(0.01));
    }

    /**
     * Test assignment with very large point values.
     * Verifies that large values are handled correctly.
     */
    @Test
    @DisplayName("Should handle very large point values")
    void shouldHandleVeryLargePointValues() {
        // Arrange & Act
        Assignment assignment = new Assignment("Large Points", 9500.0, 10000.0, GradingCategory.FINAL_EXAM);
        
        // Assert
        assertThat(assignment.getPercentageScore()).isCloseTo(95.0, within(0.01));
    }

    /**
     * Test assignment with all grading categories.
     * Verifies that assignments can be created with any grading category.
     */
    @ParameterizedTest
    @ValueSource(strings = {"HOMEWORK", "QUIZZES", "MIDTERM", "FINAL_EXAM"})
    @DisplayName("Should create assignment with any grading category")
    void shouldCreateAssignmentWithAnyGradingCategory(String categoryName) {
        // Arrange
        GradingCategory category = GradingCategory.valueOf(categoryName);
        
        // Act
        Assignment assignment = new Assignment("Test", 85.0, 100.0, category);
        
        // Assert
        assertThat(assignment.getCategory()).isEqualTo(category);
    }
}