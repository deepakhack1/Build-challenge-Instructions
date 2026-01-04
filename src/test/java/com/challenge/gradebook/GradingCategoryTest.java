package com.challenge.gradebook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the GradingCategory enum.
 * Tests enum values, default weights, weight validation, and toString method.
 */
@DisplayName("GradingCategory Enum Tests")
class GradingCategoryTest {

    // Enum Values Tests

    /**
     * Test that all expected grading categories exist.
     * Verifies that the enum contains all required category values.
     */
    @Test
    @DisplayName("Should contain all expected grading categories")
    void shouldContainAllExpectedGradingCategories() {
        // Arrange
        GradingCategory[] categories = GradingCategory.values();

        // Act & Assert
        assertThat(categories).hasSize(4);
        assertThat(categories).containsExactlyInAnyOrder(
                GradingCategory.HOMEWORK,
                GradingCategory.QUIZZES,
                GradingCategory.MIDTERM,
                GradingCategory.FINAL_EXAM
        );
    }

    /**
     * Test that each category can be retrieved by name.
     * Verifies that valueOf works correctly for all categories.
     */
    @ParameterizedTest
    @EnumSource(GradingCategory.class)
    @DisplayName("Should retrieve category by name")
    void shouldRetrieveCategoryByName(GradingCategory category) {
        // Act
        GradingCategory retrieved = GradingCategory.valueOf(category.name());

        // Assert
        assertThat(retrieved).isEqualTo(category);
    }

    // Default Weight Tests

    /**
     * Test default weights for each grading category.
     * Verifies that each category has the correct default weight.
     */
    @ParameterizedTest
    @CsvSource({
            "HOMEWORK, 20.0",
            "QUIZZES, 20.0",
            "MIDTERM, 25.0",
            "FINAL_EXAM, 35.0"
    })
    @DisplayName("Should have correct default weights")
    void shouldHaveCorrectDefaultWeights(String categoryName, double expectedWeight) {
        // Arrange
        GradingCategory category = GradingCategory.valueOf(categoryName);

        // Act
        double actualWeight = category.getDefaultWeight();

        // Assert
        assertThat(actualWeight).isEqualTo(expectedWeight);
    }

    /**
     * Test that HOMEWORK category has correct default weight.
     * Verifies specific weight value for homework assignments.
     */
    @Test
    @DisplayName("Should have correct default weight for HOMEWORK")
    void shouldHaveCorrectDefaultWeightForHomework() {
        // Act & Assert
        assertThat(GradingCategory.HOMEWORK.getDefaultWeight()).isEqualTo(20.0);
    }

    /**
     * Test that QUIZZES category has correct default weight.
     * Verifies specific weight value for quiz assignments.
     */
    @Test
    @DisplayName("Should have correct default weight for QUIZZES")
    void shouldHaveCorrectDefaultWeightForQuizzes() {
        // Act & Assert
        assertThat(GradingCategory.QUIZZES.getDefaultWeight()).isEqualTo(20.0);
    }

    /**
     * Test that MIDTERM category has correct default weight.
     * Verifies specific weight value for midterm exams.
     */
    @Test
    @DisplayName("Should have correct default weight for MIDTERM")
    void shouldHaveCorrectDefaultWeightForMidterm() {
        // Act & Assert
        assertThat(GradingCategory.MIDTERM.getDefaultWeight()).isEqualTo(25.0);
    }

    /**
     * Test that FINAL_EXAM category has correct default weight.
     * Verifies specific weight value for final exams.
     */
    @Test
    @DisplayName("Should have correct default weight for FINAL_EXAM")
    void shouldHaveCorrectDefaultWeightForFinalExam() {
        // Act & Assert
        assertThat(GradingCategory.FINAL_EXAM.getDefaultWeight()).isEqualTo(35.0);
    }

    // Weight Validation Tests

    /**
     * Test that default weights total 100%.
     * Verifies that all category weights sum to exactly 100%.
     */
    @Test
    @DisplayName("Should have default weights that total 100%")
    void shouldHaveDefaultWeightsThatTotal100Percent() {
        // Arrange
        double totalWeight = 0.0;

        // Act
        for (GradingCategory category : GradingCategory.values()) {
            totalWeight += category.getDefaultWeight();
        }

        // Assert
        assertThat(totalWeight).isEqualTo(100.0);
    }

    /**
     * Test validateDefaultWeights method returns true.
     * Verifies that the validation method correctly identifies valid weights.
     */
    @Test
    @DisplayName("Should validate that default weights are correct")
    void shouldValidateThatDefaultWeightsAreCorrect() {
        // Act
        boolean isValid = GradingCategory.validateDefaultWeights();

        // Assert
        assertThat(isValid).isTrue();
    }

    /**
     * Test weight validation with manual calculation.
     * Verifies weight validation logic with explicit calculation.
     */
    @Test
    @DisplayName("Should validate weights with manual calculation")
    void shouldValidateWeightsWithManualCalculation() {
        // Arrange
        double homework = GradingCategory.HOMEWORK.getDefaultWeight();
        double quizzes = GradingCategory.QUIZZES.getDefaultWeight();
        double midterm = GradingCategory.MIDTERM.getDefaultWeight();
        double finalExam = GradingCategory.FINAL_EXAM.getDefaultWeight();

        // Act
        double total = homework + quizzes + midterm + finalExam;

        // Assert
        assertThat(total).isCloseTo(100.0, within(0.01));
    }

    /**
     * Test that all weights are positive.
     * Verifies that no category has a negative or zero weight.
     */
    @ParameterizedTest
    @EnumSource(GradingCategory.class)
    @DisplayName("Should have positive weights for all categories")
    void shouldHavePositiveWeightsForAllCategories(GradingCategory category) {
        // Act
        double weight = category.getDefaultWeight();

        // Assert
        assertThat(weight).isPositive();
    }

    /**
     * Test that no weight exceeds 100%.
     * Verifies that individual category weights are reasonable.
     */
    @ParameterizedTest
    @EnumSource(GradingCategory.class)
    @DisplayName("Should have weights not exceeding 100%")
    void shouldHaveWeightsNotExceeding100Percent(GradingCategory category) {
        // Act
        double weight = category.getDefaultWeight();

        // Assert
        assertThat(weight).isLessThanOrEqualTo(100.0);
    }

    // ToString Tests

    /**
     * Test toString method format for each category.
     * Verifies that toString returns properly formatted string with name and weight.
     */
    @ParameterizedTest
    @CsvSource({
            "HOMEWORK, 'HOMEWORK (20.0%)'",
            "QUIZZES, 'QUIZZES (20.0%)'",
            "MIDTERM, 'MIDTERM (25.0%)'",
            "FINAL_EXAM, 'FINAL_EXAM (35.0%)'"
    })
    @DisplayName("Should return properly formatted toString")
    void shouldReturnProperlyFormattedToString(String categoryName, String expectedString) {
        // Arrange
        GradingCategory category = GradingCategory.valueOf(categoryName);

        // Act
        String result = category.toString();

        // Assert
        assertThat(result).isEqualTo(expectedString);
    }

    /**
     * Test toString method contains category name.
     * Verifies that toString includes the category name.
     */
    @ParameterizedTest
    @EnumSource(GradingCategory.class)
    @DisplayName("Should include category name in toString")
    void shouldIncludeCategoryNameInToString(GradingCategory category) {
        // Act
        String result = category.toString();

        // Assert
        assertThat(result).contains(category.name());
    }

    /**
     * Test toString method contains weight percentage.
     * Verifies that toString includes the weight percentage.
     */
    @ParameterizedTest
    @EnumSource(GradingCategory.class)
    @DisplayName("Should include weight percentage in toString")
    void shouldIncludeWeightPercentageInToString(GradingCategory category) {
        // Act
        String result = category.toString();

        // Assert
        assertThat(result)
                .contains(String.valueOf(category.getDefaultWeight()))
                .contains("%")
                .contains("(");
    }

    /**
     * Test toString method for HOMEWORK category specifically.
     * Verifies specific toString output for homework category.
     */
    @Test
    @DisplayName("Should have correct toString for HOMEWORK category")
    void shouldHaveCorrectToStringForHomeworkCategory() {
        // Act
        String result = GradingCategory.HOMEWORK.toString();

        // Assert
        assertThat(result).isEqualTo("HOMEWORK (20.0%)");
    }

    /**
     * Test toString method for FINAL_EXAM category specifically.
     * Verifies specific toString output for final exam category.
     */
    @Test
    @DisplayName("Should have correct toString for FINAL_EXAM category")
    void shouldHaveCorrectToStringForFinalExamCategory() {
        // Act
        String result = GradingCategory.FINAL_EXAM.toString();

        // Assert
        assertThat(result).isEqualTo("FINAL_EXAM (35.0%)");
    }

    // Enum Behavior Tests

    /**
     * Test enum ordinal values.
     * Verifies that enum values have expected ordinal positions.
     */
    @Test
    @DisplayName("Should have correct ordinal values")
    void shouldHaveCorrectOrdinalValues() {
        // Act & Assert
        assertThat(GradingCategory.HOMEWORK.ordinal()).isEqualTo(0);
        assertThat(GradingCategory.QUIZZES.ordinal()).isEqualTo(1);
        assertThat(GradingCategory.MIDTERM.ordinal()).isEqualTo(2);
        assertThat(GradingCategory.FINAL_EXAM.ordinal()).isEqualTo(3);
    }

    /**
     * Test enum name method.
     * Verifies that name() returns correct string values.
     */
    @ParameterizedTest
    @CsvSource({
            "HOMEWORK, HOMEWORK",
            "QUIZZES, QUIZZES",
            "MIDTERM, MIDTERM",
            "FINAL_EXAM, FINAL_EXAM"
    })
    @DisplayName("Should return correct name for each category")
    void shouldReturnCorrectNameForEachCategory(String categoryName, String expectedName) {
        // Arrange
        GradingCategory category = GradingCategory.valueOf(categoryName);

        // Act
        String actualName = category.name();

        // Assert
        assertThat(actualName).isEqualTo(expectedName);
    }

    /**
     * Test enum equality.
     * Verifies that enum instances are equal to themselves and not equal to others.
     */
    @Test
    @DisplayName("Should have correct equality behavior")
    void shouldHaveCorrectEqualityBehavior() {
        // Act & Assert
        assertThat(GradingCategory.HOMEWORK).isEqualTo(GradingCategory.HOMEWORK);
        assertThat(GradingCategory.HOMEWORK).isNotEqualTo(GradingCategory.QUIZZES);
        assertThat(GradingCategory.MIDTERM).isNotEqualTo(GradingCategory.FINAL_EXAM);
    }

    /**
     * Test that enum values are immutable.
     * Verifies that enum instances maintain their state.
     */
    @Test
    @DisplayName("Should maintain immutable state")
    void shouldMaintainImmutableState() {
        // Arrange
        GradingCategory category = GradingCategory.HOMEWORK;
        double originalWeight = category.getDefaultWeight();

        // Act - Multiple calls should return same value
        double weight1 = category.getDefaultWeight();
        double weight2 = category.getDefaultWeight();

        // Assert
        assertThat(weight1).isEqualTo(originalWeight);
        assertThat(weight2).isEqualTo(originalWeight);
        assertThat(weight1).isEqualTo(weight2);
    }

    // Edge Cases and Boundary Conditions

    /**
     * Test weight validation with floating point precision.
     * Verifies that validation handles floating point arithmetic correctly.
     */
    @Test
    @DisplayName("Should handle floating point precision in validation")
    void shouldHandleFloatingPointPrecisionInValidation() {
        // Arrange - Manually calculate total to test precision
        double total = 0.0;
        for (GradingCategory category : GradingCategory.values()) {
            total += category.getDefaultWeight();
        }

        // Act
        boolean isValid = Math.abs(total - 100.0) < 0.01;

        // Assert
        assertThat(isValid).isTrue();
        assertThat(GradingCategory.validateDefaultWeights()).isTrue();
    }

    /**
     * Test that categories can be used in switch statements.
     * Verifies that enum values work correctly in switch expressions.
     * CHANGE: Converted switch expression to traditional switch statement for Java 8 compatibility
     */
    @Test
    @DisplayName("Should work correctly in switch statements")
    void shouldWorkCorrectlyInSwitchStatements() {
        // Act & Assert
        for (GradingCategory category : GradingCategory.values()) {
            String result;
            switch (category) {
                case HOMEWORK:
                    result = "Homework assignments";
                    break;
                case QUIZZES:
                    result = "Quiz assessments";
                    break;
                case MIDTERM:
                    result = "Midterm examination";
                    break;
                case FINAL_EXAM:
                    result = "Final examination";
                    break;
                default:
                    result = "Unknown category";
                    break;
            }

            assertThat(result).isNotNull().isNotEmpty();
        }
    }

    /**
     * Test enum with collections.
     * Verifies that enum values work correctly with Java collections.
     */
    @Test
    @DisplayName("Should work correctly with collections")
    void shouldWorkCorrectlyWithCollections() {
        // Arrange
        java.util.Set<GradingCategory> categorySet = java.util.EnumSet.allOf(GradingCategory.class);

        // Act & Assert
        assertThat(categorySet).hasSize(4);
        assertThat(categorySet).containsExactlyInAnyOrder(
                GradingCategory.HOMEWORK,
                GradingCategory.QUIZZES,
                GradingCategory.MIDTERM,
                GradingCategory.FINAL_EXAM
        );
    }
}
