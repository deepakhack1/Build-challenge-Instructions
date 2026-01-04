package com.challenge.gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Course class.
 * Tests course creation, assignment management, grade calculations, and report generation.
 */
@DisplayName("Course Class Tests")
class CourseTest {

    private Course validCourse;
    private Map<GradingCategory, Double> customWeights;
    private static final String VALID_COURSE_NAME = "Computer Science 101";
    private static final int VALID_CREDIT_HOURS = 3;

    @BeforeEach
    void setUp() {
        validCourse = new Course(VALID_COURSE_NAME, VALID_CREDIT_HOURS);
        
        // Setup custom weights that total 100%
        customWeights = new EnumMap<>(GradingCategory.class);
        customWeights.put(GradingCategory.HOMEWORK, 30.0);
        customWeights.put(GradingCategory.QUIZZES, 20.0);
        customWeights.put(GradingCategory.MIDTERM, 20.0);
        customWeights.put(GradingCategory.FINAL_EXAM, 30.0);
    }

    // Course Creation Tests

    /**
     * Test successful course creation with default weights.
     * Verifies that course is created with correct properties and default category weights.
     */
    @Test
    @DisplayName("Should create course with default weights")
    void shouldCreateCourseWithDefaultWeights() {
        // Act & Assert
        assertThat(validCourse.getCourseName()).isEqualTo(VALID_COURSE_NAME);
        assertThat(validCourse.getCreditHours()).isEqualTo(VALID_CREDIT_HOURS);
        assertThat(validCourse.getAssignmentCount()).isEqualTo(0);
        
        Map<GradingCategory, Double> weights = validCourse.getCategoryWeights();
        assertThat(weights.get(GradingCategory.HOMEWORK)).isEqualTo(20.0);
        assertThat(weights.get(GradingCategory.QUIZZES)).isEqualTo(20.0);
        assertThat(weights.get(GradingCategory.MIDTERM)).isEqualTo(25.0);
        assertThat(weights.get(GradingCategory.FINAL_EXAM)).isEqualTo(35.0);
    }

    /**
     * Test successful course creation with custom weights.
     * Verifies that course is created with custom category weights.
     */
    @Test
    @DisplayName("Should create course with custom weights")
    void shouldCreateCourseWithCustomWeights() {
        // Act
        Course course = new Course("Advanced Math", 4, customWeights);
        
        // Assert
        assertThat(course.getCourseName()).isEqualTo("Advanced Math");
        assertThat(course.getCreditHours()).isEqualTo(4);
        
        Map<GradingCategory, Double> weights = course.getCategoryWeights();
        assertThat(weights.get(GradingCategory.HOMEWORK)).isEqualTo(30.0);
        assertThat(weights.get(GradingCategory.QUIZZES)).isEqualTo(20.0);
        assertThat(weights.get(GradingCategory.MIDTERM)).isEqualTo(20.0);
        assertThat(weights.get(GradingCategory.FINAL_EXAM)).isEqualTo(30.0);
    }

    /**
     * Test course creation with trimmed course name.
     * Verifies that leading and trailing whitespace is trimmed from course name.
     */
    @Test
    @DisplayName("Should trim whitespace from course name")
    void shouldTrimWhitespaceFromCourseName() {
        // Arrange
        String nameWithWhitespace = "  Physics 101  ";
        
        // Act
        Course course = new Course(nameWithWhitespace, 3);
        
        // Assert
        assertThat(course.getCourseName()).isEqualTo("Physics 101");
    }

    // Invalid Parameter Tests

    /**
     * Test course creation with null name.
     * Verifies that IllegalArgumentException is thrown for null course name.
     */
    @Test
    @DisplayName("Should throw exception for null course name")
    void shouldThrowExceptionForNullCourseName() {
        // Act & Assert
        assertThatThrownBy(() -> new Course(null, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course name cannot be null or empty");
    }

    /**
     * Test course creation with empty name.
     * Verifies that IllegalArgumentException is thrown for empty course name.
     */
    @Test
    @DisplayName("Should throw exception for empty course name")
    void shouldThrowExceptionForEmptyCourseName() {
        // Act & Assert
        assertThatThrownBy(() -> new Course("", 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course name cannot be null or empty");
    }

    /**
     * Test course creation with whitespace-only name.
     * Verifies that IllegalArgumentException is thrown for whitespace-only course name.
     */
    @Test
    @DisplayName("Should throw exception for whitespace-only course name")
    void shouldThrowExceptionForWhitespaceOnlyCourseName() {
        // Act & Assert
        assertThatThrownBy(() -> new Course("   ", 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course name cannot be null or empty");
    }

    /**
     * Test course creation with zero credit hours.
     * Verifies that IllegalArgumentException is thrown for zero credit hours.
     */
    @Test
    @DisplayName("Should throw exception for zero credit hours")
    void shouldThrowExceptionForZeroCreditHours() {
        // Act & Assert
        assertThatThrownBy(() -> new Course("Test Course", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credit hours must be positive");
    }

    /**
     * Test course creation with negative credit hours.
     * Verifies that IllegalArgumentException is thrown for negative credit hours.
     */
    @Test
    @DisplayName("Should throw exception for negative credit hours")
    void shouldThrowExceptionForNegativeCreditHours() {
        // Act & Assert
        assertThatThrownBy(() -> new Course("Test Course", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credit hours must be positive");
    }

    /**
     * Test course creation with null category weights.
     * Verifies that IllegalArgumentException is thrown for null weights.
     */
    @Test
    @DisplayName("Should throw exception for null category weights")
    void shouldThrowExceptionForNullCategoryWeights() {
        // Act & Assert
        assertThatThrownBy(() -> new Course("Test Course", 3, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category weights cannot be null or empty");
    }

    /**
     * Test course creation with empty category weights.
     * Verifies that IllegalArgumentException is thrown for empty weights map.
     */
    @Test
    @DisplayName("Should throw exception for empty category weights")
    void shouldThrowExceptionForEmptyCategoryWeights() {
        // Arrange
        Map<GradingCategory, Double> emptyWeights = new EnumMap<>(GradingCategory.class);
        
        // Act & Assert
        assertThatThrownBy(() -> new Course("Test Course", 3, emptyWeights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category weights cannot be null or empty");
    }

    /**
     * Test course creation with weights not totaling 100%.
     * Verifies that IllegalArgumentException is thrown when weights don't sum to 100%.
     */
    @Test
    @DisplayName("Should throw exception when weights don't total 100%")
    void shouldThrowExceptionWhenWeightsDontTotal100Percent() {
        // Arrange
        Map<GradingCategory, Double> invalidWeights = new EnumMap<>(GradingCategory.class);
        invalidWeights.put(GradingCategory.HOMEWORK, 30.0);
        invalidWeights.put(GradingCategory.QUIZZES, 20.0);
        invalidWeights.put(GradingCategory.MIDTERM, 20.0);
        invalidWeights.put(GradingCategory.FINAL_EXAM, 20.0); // Total = 90%
        
        // Act & Assert
        assertThatThrownBy(() -> new Course("Test Course", 3, invalidWeights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category weights must total 100%, got: 90.0");
    }

    // Assignment Management Tests

    /**
     * Test adding valid assignment to course.
     * Verifies that assignments can be added and retrieved correctly.
     */
    @Test
    @DisplayName("Should add valid assignment to course")
    void shouldAddValidAssignmentToCourse() {
        // Arrange
        Assignment assignment = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act
        validCourse.addAssignment(assignment);
        
        // Assert
        assertThat(validCourse.getAssignmentCount()).isEqualTo(1);
        assertThat(validCourse.getAssignments()).contains(assignment);
    }

    /**
     * Test adding multiple assignments to course.
     * Verifies that multiple assignments can be added and tracked.
     */
    @Test
    @DisplayName("Should add multiple assignments to course")
    void shouldAddMultipleAssignmentsToCourse() {
        // Arrange
        Assignment hw1 = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment quiz1 = new Assignment("Quiz 1", 90.0, 100.0, GradingCategory.QUIZZES);
        Assignment midterm = new Assignment("Midterm", 78.0, 100.0, GradingCategory.MIDTERM);
        
        // Act
        validCourse.addAssignment(hw1);
        validCourse.addAssignment(quiz1);
        validCourse.addAssignment(midterm);
        
        // Assert
        assertThat(validCourse.getAssignmentCount()).isEqualTo(3);
        assertThat(validCourse.getAssignments()).containsExactly(hw1, quiz1, midterm);
    }

    /**
     * Test adding null assignment.
     * Verifies that IllegalArgumentException is thrown for null assignment.
     */
    @Test
    @DisplayName("Should throw exception for null assignment")
    void shouldThrowExceptionForNullAssignment() {
        // Act & Assert
        assertThatThrownBy(() -> validCourse.addAssignment(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assignment cannot be null");
    }

    /**
     * Test getting assignments by category.
     * Verifies that assignments can be filtered by grading category.
     */
    @Test
    @DisplayName("Should get assignments by category")
    void shouldGetAssignmentsByCategory() {
        // Arrange
        Assignment hw1 = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment hw2 = new Assignment("Homework 2", 90.0, 100.0, GradingCategory.HOMEWORK);
        Assignment quiz1 = new Assignment("Quiz 1", 95.0, 100.0, GradingCategory.QUIZZES);
        
        validCourse.addAssignment(hw1);
        validCourse.addAssignment(hw2);
        validCourse.addAssignment(quiz1);
        
        // Act
        List<Assignment> homeworkAssignments = validCourse.getAssignmentsByCategory(GradingCategory.HOMEWORK);
        List<Assignment> quizAssignments = validCourse.getAssignmentsByCategory(GradingCategory.QUIZZES);
        
        // Assert
        assertThat(homeworkAssignments).hasSize(2).containsExactly(hw1, hw2);
        assertThat(quizAssignments).hasSize(1).containsExactly(quiz1);
    }

    /**
     * Test getting assignments for empty category.
     * Verifies that empty list is returned for categories with no assignments.
     */
    @Test
    @DisplayName("Should return empty list for category with no assignments")
    void shouldReturnEmptyListForCategoryWithNoAssignments() {
        // Act
        List<Assignment> assignments = validCourse.getAssignmentsByCategory(GradingCategory.MIDTERM);
        
        // Assert
        assertThat(assignments).isEmpty();
    }

    // Category Average Calculation Tests

    /**
     * Test category average calculation with single assignment.
     * Verifies correct average calculation for single assignment in category.
     */
    @Test
    @DisplayName("Should calculate category average with single assignment")
    void shouldCalculateCategoryAverageWithSingleAssignment() {
        // Arrange
        Assignment assignment = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        validCourse.addAssignment(assignment);
        
        // Act
        double average = validCourse.getCategoryAverage(GradingCategory.HOMEWORK);
        
        // Assert
        assertThat(average).isEqualTo(85.0);
    }

    /**
     * Test category average calculation with multiple assignments.
     * Verifies correct weighted average calculation for multiple assignments.
     */
    @Test
    @DisplayName("Should calculate category average with multiple assignments")
    void shouldCalculateCategoryAverageWithMultipleAssignments() {
        // Arrange
        Assignment hw1 = new Assignment("Homework 1", 80.0, 100.0, GradingCategory.HOMEWORK); // 80%
        Assignment hw2 = new Assignment("Homework 2", 45.0, 50.0, GradingCategory.HOMEWORK);  // 90%
        validCourse.addAssignment(hw1);
        validCourse.addAssignment(hw2);
        
        // Act
        double average = validCourse.getCategoryAverage(GradingCategory.HOMEWORK);
        
        // Assert
        // Total points: 80 + 45 = 125, Total possible: 100 + 50 = 150
        // Average: (125/150) * 100 = 83.33%
        assertThat(average).isCloseTo(83.33, within(0.01));
    }

    /**
     * Test category average for empty category.
     * Verifies that 0.0 is returned for categories with no assignments.
     */
    @Test
    @DisplayName("Should return 0.0 for empty category average")
    void shouldReturn0ForEmptyCategoryAverage() {
        // Act
        double average = validCourse.getCategoryAverage(GradingCategory.HOMEWORK);
        
        // Assert
        assertThat(average).isEqualTo(0.0);
    }

    /**
     * Test category average with zero total possible points.
     * Verifies edge case handling when total possible points is zero.
     */
    @Test
    @DisplayName("Should handle zero total possible points in category")
    void shouldHandleZeroTotalPossiblePointsInCategory() {
        // This test verifies the edge case, though it's unlikely in practice
        // since Assignment constructor prevents zero points possible
        
        // Arrange - Add assignment with minimal points
        Assignment assignment = new Assignment("Test", 0.0, 0.1, GradingCategory.HOMEWORK);
        validCourse.addAssignment(assignment);
        
        // Act
        double average = validCourse.getCategoryAverage(GradingCategory.HOMEWORK);
        
        // Assert
        assertThat(average).isEqualTo(0.0);
    }

    // Final Grade Calculation Tests

    /**
     * Test final grade calculation with all categories having assignments.
     * Verifies correct weighted final grade calculation.
     */
    @Test
    @DisplayName("Should calculate final grade with all categories")
    void shouldCalculateFinalGradeWithAllCategories() {
        // Arrange
        validCourse.addAssignment(new Assignment("HW1", 80.0, 100.0, GradingCategory.HOMEWORK));     // 80% * 20% = 16%
        validCourse.addAssignment(new Assignment("Quiz1", 90.0, 100.0, GradingCategory.QUIZZES));     // 90% * 20% = 18%
        validCourse.addAssignment(new Assignment("Midterm", 85.0, 100.0, GradingCategory.MIDTERM));   // 85% * 25% = 21.25%
        validCourse.addAssignment(new Assignment("Final", 88.0, 100.0, GradingCategory.FINAL_EXAM));  // 88% * 35% = 30.8%
        
        // Act
        double finalGrade = validCourse.calculateFinalGrade();
        
        // Assert
        // Total: 16% + 18% + 21.25% + 30.8% = 86.05%
        assertThat(finalGrade).isCloseTo(86.05, within(0.01));
    }

    /**
     * Test final grade calculation with missing categories.
     * Verifies that missing categories are excluded from calculation.
     */
    @Test
    @DisplayName("Should calculate final grade with missing categories")
    void shouldCalculateFinalGradeWithMissingCategories() {
        // Arrange - Only add homework and quiz assignments
        validCourse.addAssignment(new Assignment("HW1", 80.0, 100.0, GradingCategory.HOMEWORK));   // 80%
        validCourse.addAssignment(new Assignment("Quiz1", 90.0, 100.0, GradingCategory.QUIZZES));   // 90%
        
        // Act
        double finalGrade = validCourse.calculateFinalGrade();
        
        // Assert
        // Only homework (20%) and quizzes (20%) = 40% total weight
        // Weighted average: (80% * 20% + 90% * 20%) / 40% = (16% + 18%) / 40% = 34% / 40% = 85%
        assertThat(finalGrade).isCloseTo(85.0, within(0.01));
    }

    /**
     * Test final grade calculation with no assignments.
     * Verifies that 0.0 is returned when no assignments exist.
     */
    @Test
    @DisplayName("Should return 0.0 for final grade with no assignments")
    void shouldReturn0ForFinalGradeWithNoAssignments() {
        // Act
        double finalGrade = validCourse.calculateFinalGrade();
        
        // Assert
        assertThat(finalGrade).isEqualTo(0.0);
    }

    /**
     * Test final grade calculation with custom weights.
     * Verifies that custom category weights are used correctly.
     */
    @Test
    @DisplayName("Should calculate final grade with custom weights")
    void shouldCalculateFinalGradeWithCustomWeights() {
        // Arrange
        Course customCourse = new Course("Custom Course", 3, customWeights);
        customCourse.addAssignment(new Assignment("HW1", 80.0, 100.0, GradingCategory.HOMEWORK));     // 80% * 30% = 24%
        customCourse.addAssignment(new Assignment("Quiz1", 90.0, 100.0, GradingCategory.QUIZZES));     // 90% * 20% = 18%
        customCourse.addAssignment(new Assignment("Midterm", 85.0, 100.0, GradingCategory.MIDTERM));   // 85% * 20% = 17%
        customCourse.addAssignment(new Assignment("Final", 88.0, 100.0, GradingCategory.FINAL_EXAM));  // 88% * 30% = 26.4%
        
        // Act
        double finalGrade = customCourse.calculateFinalGrade();
        
        // Assert
        // Total: 24% + 18% + 17% + 26.4% = 85.4%
        assertThat(finalGrade).isCloseTo(85.4, within(0.01));
    }

    // Letter Grade and GPA Tests

    /**
     * Test letter grade conversion for various percentages.
     * Verifies correct letter grade assignment for different percentage ranges.
     */
    @ParameterizedTest
    @CsvSource({
            "95.0, A",
            "90.0, A",
            "89.9, B",
            "85.0, B",
            "80.0, B",
            "79.9, C",
            "75.0, C",
            "70.0, C",
            "69.9, D",
            "65.0, D",
            "60.0, D",
            "59.9, F",
            "50.0, F",
            "0.0, F"
    })
    @DisplayName("Should return correct letter grade for percentage")
    void shouldReturnCorrectLetterGradeForPercentage(double percentage, String expectedGrade) {
        // Act
        String letterGrade = Course.getLetterGrade(percentage);
        
        // Assert
        assertThat(letterGrade).isEqualTo(expectedGrade);
    }

    /**
     * Test GPA points conversion for letter grades.
     * Verifies correct GPA points assignment for different letter grades.
     */
    @ParameterizedTest
    @CsvSource({
            "A, 4.0",
            "a, 4.0",
            "B, 3.0",
            "b, 3.0",
            "C, 2.0",
            "c, 2.0",
            "D, 1.0",
            "d, 1.0",
            "F, 0.0",
            "f, 0.0"
    })
    @DisplayName("Should return correct GPA points for letter grade")
    void shouldReturnCorrectGpaPointsForLetterGrade(String letterGrade, double expectedPoints) {
        // Act
        double gpaPoints = Course.getGpaPoints(letterGrade);
        
        // Assert
        assertThat(gpaPoints).isEqualTo(expectedPoints);
    }

    /**
     * Test GPA points with invalid letter grade.
     * Verifies that IllegalArgumentException is thrown for invalid letter grades.
     */
    @Test
    @DisplayName("Should throw exception for invalid letter grade")
    void shouldThrowExceptionForInvalidLetterGrade() {
        // Act & Assert
        assertThatThrownBy(() -> Course.getGpaPoints("X"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid letter grade: X");
    }

    /**
     * Test final letter grade for course.
     * Verifies that final letter grade is calculated correctly based on final percentage.
     */
    @Test
    @DisplayName("Should return correct final letter grade for course")
    void shouldReturnCorrectFinalLetterGradeForCourse() {
        // Arrange - Set up course with 85% final grade
        validCourse.addAssignment(new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK));
        
        // Act
        String letterGrade = validCourse.getFinalLetterGrade();
        
        // Assert
        assertThat(letterGrade).isEqualTo("B");
    }

    /**
     * Test GPA points for course.
     * Verifies that GPA points are calculated correctly based on final letter grade.
     */
    @Test
    @DisplayName("Should return correct GPA points for course")
    void shouldReturnCorrectGpaPointsForCourse() {
        // Arrange - Set up course with 92% final grade (A)
        validCourse.addAssignment(new Assignment("Test", 92.0, 100.0, GradingCategory.HOMEWORK));
        
        // Act
        double gpaPoints = validCourse.getGpaPoints();
        
        // Assert
        assertThat(gpaPoints).isEqualTo(4.0);
    }

    // Course Report Generation Tests

    /**
     * Test course report generation with assignments.
     * Verifies that course report contains all expected information.
     */
    @Test
    @DisplayName("Should generate comprehensive course report")
    void shouldGenerateComprehensiveCourseReport() {
        // Arrange
        validCourse.addAssignment(new Assignment("HW1", 85.0, 100.0, GradingCategory.HOMEWORK));
        validCourse.addAssignment(new Assignment("Quiz1", 90.0, 100.0, GradingCategory.QUIZZES));
        
        // Act
        String report = validCourse.generateCourseReport();
        
        // Assert
        assertThat(report)
                .contains("COURSE REPORT")
                .contains("Course: " + VALID_COURSE_NAME)
                .contains("Credit Hours: " + VALID_CREDIT_HOURS)
                .contains("Final Grade:")
                .contains("GPA Points:")
                .contains("Category Breakdown:")
                .contains("Assignments:")
                .contains("HOMEWORK")
                .contains("QUIZZES")
                .contains("HW1")
                .contains("Quiz1");
    }

    /**
     * Test course report generation with no assignments.
     * Verifies that course report handles empty assignment list correctly.
     */
    @Test
    @DisplayName("Should generate course report with no assignments")
    void shouldGenerateCourseReportWithNoAssignments() {
        // Act
        String report = validCourse.generateCourseReport();
        
        // Assert
        assertThat(report)
                .contains("COURSE REPORT")
                .contains("Course: " + VALID_COURSE_NAME)
                .contains("Final Grade: 0.0%")
                .contains("[0 assignments]");
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
        assertThat(validCourse.getCourseName()).isEqualTo(VALID_COURSE_NAME);
        assertThat(validCourse.getCreditHours()).isEqualTo(VALID_CREDIT_HOURS);
        assertThat(validCourse.getAssignmentCount()).isEqualTo(0);
        assertThat(validCourse.getAssignments()).isEmpty();
        assertThat(validCourse.getCategoryWeights()).isNotNull().hasSize(4);
    }

    /**
     * Test that getters return defensive copies.
     * Verifies that returned collections cannot modify internal state.
     */
    @Test
    @DisplayName("Should return defensive copies from getters")
    void shouldReturnDefensiveCopiesFromGetters() {
        // Arrange
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        validCourse.addAssignment(assignment);
        
        // Act
        List<Assignment> assignments = validCourse.getAssignments();
        Map<GradingCategory, Double> weights = validCourse.getCategoryWeights();
        
        // Assert - Modifying returned collections should not affect course
        assignments.clear();
        weights.clear();
        
        assertThat(validCourse.getAssignmentCount()).isEqualTo(1);
        assertThat(validCourse.getCategoryWeights()).hasSize(4);
    }

    // Equals and HashCode Tests

    /**
     * Test equals method with identical courses.
     * Verifies that courses with same name are equal.
     */
    @Test
    @DisplayName("Should be equal when course names match")
    void shouldBeEqualWhenCourseNamesMatch() {
        // Arrange
        Course course1 = new Course("Math 101", 3);
        Course course2 = new Course("Math 101", 4); // Different credit hours
        
        // Act & Assert
        assertThat(course1).isEqualTo(course2);
        assertThat(course1.hashCode()).isEqualTo(course2.hashCode());
    }

    /**
     * Test equals method with different course names.
     * Verifies that courses with different names are not equal.
     */
    @Test
    @DisplayName("Should not be equal when course names differ")
    void shouldNotBeEqualWhenCourseNamesDiffer() {
        // Arrange
        Course course1 = new Course("Math 101", 3);
        Course course2 = new Course("Math 102", 3);
        
        // Act & Assert
        assertThat(course1).isNotEqualTo(course2);
    }

    /**
     * Test equals method with null object.
     * Verifies that course is not equal to null.
     */
    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Act & Assert
        assertThat(validCourse).isNotEqualTo(null);
    }

    /**
     * Test equals method reflexivity.
     * Verifies that a course equals itself.
     */
    @Test
    @DisplayName("Should be equal to itself (reflexivity)")
    void shouldBeEqualToItself() {
        // Act & Assert
        assertThat(validCourse).isEqualTo(validCourse);
    }

    // ToString Tests

    /**
     * Test toString method format and content.
     * Verifies that toString returns properly formatted string with course information.
     */
    @Test
    @DisplayName("Should return properly formatted toString")
    void shouldReturnProperlyFormattedToString() {
        // Arrange
        validCourse.addAssignment(new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK));
        
        // Act
        String result = validCourse.toString();
        
        // Assert
        assertThat(result)
                .contains("Course{")
                .contains("name='" + VALID_COURSE_NAME + "'")
                .contains("creditHours=" + VALID_CREDIT_HOURS)
                .contains("assignments=1")
                .contains("finalGrade=")
                .contains("(B)");
    }

    // Edge Cases and Boundary Conditions

    /**
     * Test course with maximum credit hours.
     * Verifies that large credit hour values are handled correctly.
     */
    @Test
    @DisplayName("Should handle maximum credit hours")
    void shouldHandleMaximumCreditHours() {
        // Act
        Course course = new Course("Intensive Course", Integer.MAX_VALUE);
        
        // Assert
        assertThat(course.getCreditHours()).isEqualTo(Integer.MAX_VALUE);
    }

    /**
     * Test category weights with floating point precision.
     * Verifies that floating point arithmetic is handled correctly.
     */
    @Test
    @DisplayName("Should handle floating point precision in weights")
    void shouldHandleFloatingPointPrecisionInWeights() {
        // Arrange
        Map<GradingCategory, Double> precisionWeights = new EnumMap<>(GradingCategory.class);
        precisionWeights.put(GradingCategory.HOMEWORK, 25.0);
        precisionWeights.put(GradingCategory.QUIZZES, 25.0);
        precisionWeights.put(GradingCategory.MIDTERM, 25.0);
        precisionWeights.put(GradingCategory.FINAL_EXAM, 25.0);
        
        // Act & Assert - Should not throw exception
        assertThatCode(() -> new Course("Test", 3, precisionWeights))
                .doesNotThrowAnyException();
    }

    /**
     * Test course with very long name.
     * Verifies that long course names are handled correctly.
     */
    @Test
    @DisplayName("Should handle very long course name")
    void shouldHandleVeryLongCourseName() {
        // Arrange
        String longName = "A".repeat(1000);
        
        // Act
        Course course = new Course(longName, 3);
        
        // Assert
        assertThat(course.getCourseName()).hasSize(1000);
    }

    /**
     * Test boundary conditions for grade calculations.
     * Verifies edge cases in percentage and letter grade boundaries.
     */
    @ParameterizedTest
    @ValueSource(doubles = {59.99, 60.0, 60.01, 69.99, 70.0, 70.01, 79.99, 80.0, 80.01, 89.99, 90.0, 90.01})
    @DisplayName("Should handle grade boundary conditions correctly")
    void shouldHandleGradeBoundaryConditionsCorrectly(double percentage) {
        // Act
        String letterGrade = Course.getLetterGrade(percentage);
        double gpaPoints = Course.getGpaPoints(letterGrade);
        
        // Assert
        assertThat(letterGrade).isIn("A", "B", "C", "D", "F");
        assertThat(gpaPoints).isBetween(0.0, 4.0);
    }
}