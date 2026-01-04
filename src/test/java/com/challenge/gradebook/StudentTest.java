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
 * Comprehensive unit tests for the Student class.
 * Tests student creation, course enrollment, assignment management, and grade calculations.
 */
@DisplayName("Student Class Tests")
class StudentTest {

    private Student validStudent;
    private Course mathCourse;
    private Course scienceCourse;
    private static final String VALID_STUDENT_ID = "STU001";
    private static final String VALID_STUDENT_NAME = "John Doe";

    @BeforeEach
    void setUp() {
        validStudent = new Student(VALID_STUDENT_ID, VALID_STUDENT_NAME);
        mathCourse = new Course("Mathematics 101", 3);
        scienceCourse = new Course("Science 101", 4);
    }

    // Student Creation and Validation Tests

    /**
     * Test successful student creation with valid parameters.
     * Verifies that student is created with correct ID and name.
     */
    @Test
    @DisplayName("Should create student with valid parameters")
    void shouldCreateStudentWithValidParameters() {
        // Act & Assert
        assertThat(validStudent.getStudentId()).isEqualTo(VALID_STUDENT_ID);
        assertThat(validStudent.getName()).isEqualTo(VALID_STUDENT_NAME);
        assertThat(validStudent.getCourseCount()).isEqualTo(0);
        assertThat(validStudent.getCourses()).isEmpty();
    }

    /**
     * Test student creation with whitespace in ID and name.
     * Verifies that leading and trailing whitespace is trimmed.
     */
    @Test
    @DisplayName("Should trim whitespace from student ID and name")
    void shouldTrimWhitespaceFromStudentIdAndName() {
        // Arrange
        String idWithWhitespace = "  STU002  ";
        String nameWithWhitespace = "  Jane Smith  ";
        
        // Act
        Student student = new Student(idWithWhitespace, nameWithWhitespace);
        
        // Assert
        assertThat(student.getStudentId()).isEqualTo("STU002");
        assertThat(student.getName()).isEqualTo("Jane Smith");
    }

    /**
     * Test student creation with null student ID.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for null student ID")
    void shouldThrowExceptionForNullStudentId() {
        // Act & Assert
        assertThatThrownBy(() -> new Student(null, "John Doe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID cannot be null or empty");
    }

    /**
     * Test student creation with empty student ID.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for empty student ID")
    void shouldThrowExceptionForEmptyStudentId() {
        // Act & Assert
        assertThatThrownBy(() -> new Student("", "John Doe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID cannot be null or empty");
    }

    /**
     * Test student creation with whitespace-only student ID.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for whitespace-only student ID")
    void shouldThrowExceptionForWhitespaceOnlyStudentId() {
        // Act & Assert
        assertThatThrownBy(() -> new Student("   ", "John Doe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID cannot be null or empty");
    }

    /**
     * Test student creation with null name.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for null student name")
    void shouldThrowExceptionForNullStudentName() {
        // Act & Assert
        assertThatThrownBy(() -> new Student("STU001", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student name cannot be null or empty");
    }

    /**
     * Test student creation with empty name.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for empty student name")
    void shouldThrowExceptionForEmptyStudentName() {
        // Act & Assert
        assertThatThrownBy(() -> new Student("STU001", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student name cannot be null or empty");
    }

    /**
     * Test student creation with whitespace-only name.
     * Verifies that IllegalArgumentException is thrown.
     */
    @Test
    @DisplayName("Should throw exception for whitespace-only student name")
    void shouldThrowExceptionForWhitespaceOnlyStudentName() {
        // Act & Assert
        assertThatThrownBy(() -> new Student("STU001", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student name cannot be null or empty");
    }

    // Course Enrollment Tests

    /**
     * Test successful course enrollment.
     * Verifies that student can be enrolled in a course.
     */
    @Test
    @DisplayName("Should enroll student in course successfully")
    void shouldEnrollStudentInCourseSuccessfully() {
        // Act
        validStudent.enrollInCourse(mathCourse);
        
        // Assert
        assertThat(validStudent.getCourseCount()).isEqualTo(1);
        assertThat(validStudent.getCourses()).contains(mathCourse);
        assertThat(validStudent.isEnrolledIn("Mathematics 101")).isTrue();
    }

    /**
     * Test enrollment in multiple courses.
     * Verifies that student can be enrolled in multiple courses.
     */
    @Test
    @DisplayName("Should enroll student in multiple courses")
    void shouldEnrollStudentInMultipleCourses() {
        // Act
        validStudent.enrollInCourse(mathCourse);
        validStudent.enrollInCourse(scienceCourse);
        
        // Assert
        assertThat(validStudent.getCourseCount()).isEqualTo(2);
        assertThat(validStudent.getCourses()).containsExactly(mathCourse, scienceCourse);
        assertThat(validStudent.isEnrolledIn("Mathematics 101")).isTrue();
        assertThat(validStudent.isEnrolledIn("Science 101")).isTrue();
    }

    /**
     * Test enrollment with null course.
     * Verifies that IllegalArgumentException is thrown for null course.
     */
    @Test
    @DisplayName("Should throw exception for null course enrollment")
    void shouldThrowExceptionForNullCourseEnrollment() {
        // Act & Assert
        assertThatThrownBy(() -> validStudent.enrollInCourse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course cannot be null");
    }

    /**
     * Test duplicate course enrollment.
     * Verifies that IllegalArgumentException is thrown for duplicate enrollment.
     */
    @Test
    @DisplayName("Should throw exception for duplicate course enrollment")
    void shouldThrowExceptionForDuplicateCourseEnrollment() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        
        // Act & Assert
        assertThatThrownBy(() -> validStudent.enrollInCourse(mathCourse))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is already enrolled in course: Mathematics 101");
    }

    /**
     * Test getting course by name.
     * Verifies that enrolled courses can be retrieved by name.
     */
    @Test
    @DisplayName("Should get course by name")
    void shouldGetCourseByName() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        validStudent.enrollInCourse(scienceCourse);
        
        // Act
        Course retrievedMath = validStudent.getCourse("Mathematics 101");
        Course retrievedScience = validStudent.getCourse("Science 101");
        Course nonExistent = validStudent.getCourse("Physics 101");
        
        // Assert
        assertThat(retrievedMath).isEqualTo(mathCourse);
        assertThat(retrievedScience).isEqualTo(scienceCourse);
        assertThat(nonExistent).isNull();
    }

    /**
     * Test case-insensitive course name lookup.
     * Verifies that course lookup is case-insensitive.
     */
    @Test
    @DisplayName("Should get course by name case-insensitively")
    void shouldGetCourseByNameCaseInsensitively() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        
        // Act
        Course retrieved1 = validStudent.getCourse("mathematics 101");
        Course retrieved2 = validStudent.getCourse("MATHEMATICS 101");
        Course retrieved3 = validStudent.getCourse("Mathematics 101");
        
        // Assert
        assertThat(retrieved1).isEqualTo(mathCourse);
        assertThat(retrieved2).isEqualTo(mathCourse);
        assertThat(retrieved3).isEqualTo(mathCourse);
    }

    /**
     * Test enrollment status check.
     * Verifies that enrollment status can be checked correctly.
     */
    @Test
    @DisplayName("Should check enrollment status correctly")
    void shouldCheckEnrollmentStatusCorrectly() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        
        // Act & Assert
        assertThat(validStudent.isEnrolledIn("Mathematics 101")).isTrue();
        assertThat(validStudent.isEnrolledIn("Science 101")).isFalse();
        assertThat(validStudent.isEnrolledIn("Physics 101")).isFalse();
    }

    // Assignment Addition Tests

    /**
     * Test adding assignment to enrolled course.
     * Verifies that assignments can be added to courses the student is enrolled in.
     */
    @Test
    @DisplayName("Should add assignment to enrolled course")
    void shouldAddAssignmentToEnrolledCourse() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment assignment = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act
        validStudent.addAssignment("Mathematics 101", assignment);
        
        // Assert
        Course course = validStudent.getCourse("Mathematics 101");
        assertThat(course.getAssignments()).contains(assignment);
        assertThat(course.getAssignmentCount()).isEqualTo(1);
    }

    /**
     * Test adding assignment to non-enrolled course.
     * Verifies that IllegalArgumentException is thrown when adding assignment to non-enrolled course.
     */
    @Test
    @DisplayName("Should throw exception when adding assignment to non-enrolled course")
    void shouldThrowExceptionWhenAddingAssignmentToNonEnrolledCourse() {
        // Arrange
        Assignment assignment = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThatThrownBy(() -> validStudent.addAssignment("Mathematics 101", assignment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Mathematics 101");
    }

    /**
     * Test adding multiple assignments to course.
     * Verifies that multiple assignments can be added to the same course.
     */
    @Test
    @DisplayName("Should add multiple assignments to course")
    void shouldAddMultipleAssignmentsToCourse() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment hw1 = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment quiz1 = new Assignment("Quiz 1", 90.0, 100.0, GradingCategory.QUIZZES);
        
        // Act
        validStudent.addAssignment("Mathematics 101", hw1);
        validStudent.addAssignment("Mathematics 101", quiz1);
        
        // Assert
        Course course = validStudent.getCourse("Mathematics 101");
        assertThat(course.getAssignments()).containsExactly(hw1, quiz1);
        assertThat(course.getAssignmentCount()).isEqualTo(2);
    }

    // Grade Calculation Tests

    /**
     * Test category average calculation.
     * Verifies that category averages are calculated correctly for student's course.
     */
    @Test
    @DisplayName("Should calculate category average correctly")
    void shouldCalculateCategoryAverageCorrectly() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment hw1 = new Assignment("HW1", 80.0, 100.0, GradingCategory.HOMEWORK);
        Assignment hw2 = new Assignment("HW2", 90.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", hw1);
        validStudent.addAssignment("Mathematics 101", hw2);
        
        // Act
        double average = validStudent.getCategoryAverage("Mathematics 101", GradingCategory.HOMEWORK);
        
        // Assert
        assertThat(average).isEqualTo(85.0); // (80 + 90) / 2 = 85
    }

    /**
     * Test category average for non-enrolled course.
     * Verifies that IllegalArgumentException is thrown for non-enrolled course.
     */
    @Test
    @DisplayName("Should throw exception for category average of non-enrolled course")
    void shouldThrowExceptionForCategoryAverageOfNonEnrolledCourse() {
        // Act & Assert
        assertThatThrownBy(() -> validStudent.getCategoryAverage("Mathematics 101", GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Mathematics 101");
    }

    /**
     * Test course grade calculation.
     * Verifies that course grades are calculated correctly.
     */
    @Test
    @DisplayName("Should calculate course grade correctly")
    void shouldCalculateCourseGradeCorrectly() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment hw = new Assignment("HW", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment quiz = new Assignment("Quiz", 90.0, 100.0, GradingCategory.QUIZZES);
        Assignment midterm = new Assignment("Midterm", 80.0, 100.0, GradingCategory.MIDTERM);
        Assignment finalExam = new Assignment("Final", 88.0, 100.0, GradingCategory.FINAL_EXAM);
        
        validStudent.addAssignment("Mathematics 101", hw);
        validStudent.addAssignment("Mathematics 101", quiz);
        validStudent.addAssignment("Mathematics 101", midterm);
        validStudent.addAssignment("Mathematics 101", finalExam);
        
        // Act
        Student.CourseGrade grade = validStudent.getCourseGrade("Mathematics 101");
        
        // Assert
        // Expected: 85*0.2 + 90*0.2 + 80*0.25 + 88*0.35 = 17 + 18 + 20 + 30.8 = 85.8%
        assertThat(grade.getPercentage()).isCloseTo(85.8, within(0.1));
        assertThat(grade.getLetterGrade()).isEqualTo("B");
    }

    /**
     * Test course grade for non-enrolled course.
     * Verifies that IllegalArgumentException is thrown for non-enrolled course.
     */
    @Test
    @DisplayName("Should throw exception for course grade of non-enrolled course")
    void shouldThrowExceptionForCourseGradeOfNonEnrolledCourse() {
        // Act & Assert
        assertThatThrownBy(() -> validStudent.getCourseGrade("Mathematics 101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Mathematics 101");
    }

    // GPA Calculation Tests

    /**
     * Test GPA calculation with no courses.
     * Verifies that GPA is 0.0 when student has no courses.
     */
    @Test
    @DisplayName("Should return 0.0 GPA when no courses enrolled")
    void shouldReturn0GpaWhenNoCoursesEnrolled() {
        // Act
        double gpa = validStudent.calculateGPA();
        
        // Assert
        assertThat(gpa).isEqualTo(0.0);
    }

    /**
     * Test GPA calculation with single course.
     * Verifies that GPA equals the course grade when only one course is enrolled.
     */
    @Test
    @DisplayName("Should calculate GPA with single course")
    void shouldCalculateGpaWithSingleCourse() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", assignment);
        
        // Act
        double gpa = validStudent.calculateGPA();
        
        // Assert
        // 85% = B = 3.0 GPA points
        assertThat(gpa).isEqualTo(3.0);
    }

    /**
     * Test GPA calculation weighted by credit hours.
     * Verifies that GPA is correctly weighted by credit hours across multiple courses.
     */
    @Test
    @DisplayName("Should calculate GPA weighted by credit hours")
    void shouldCalculateGpaWeightedByCreditHours() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);    // 3 credit hours
        validStudent.enrollInCourse(scienceCourse); // 4 credit hours
        
        // Math course: 90% = A = 4.0 GPA points
        Assignment mathAssignment = new Assignment("Math Test", 90.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", mathAssignment);
        
        // Science course: 80% = B = 3.0 GPA points
        Assignment scienceAssignment = new Assignment("Science Test", 80.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Science 101", scienceAssignment);
        
        // Act
        double gpa = validStudent.calculateGPA();
        
        // Assert
        // Weighted GPA: (4.0 * 3 + 3.0 * 4) / (3 + 4) = (12 + 12) / 7 = 24/7 ≈ 3.43
        assertThat(gpa).isCloseTo(3.43, within(0.01));
    }

    /**
     * Test GPA calculation with multiple courses of same credit hours.
     * Verifies GPA calculation when all courses have equal weight.
     */
    @Test
    @DisplayName("Should calculate GPA with equal credit hour courses")
    void shouldCalculateGpaWithEqualCreditHourCourses() {
        // Arrange
        Course course1 = new Course("Course 1", 3);
        Course course2 = new Course("Course 2", 3);
        validStudent.enrollInCourse(course1);
        validStudent.enrollInCourse(course2);
        
        // Course 1: 95% = A = 4.0 GPA
        Assignment assignment1 = new Assignment("Test1", 95.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Course 1", assignment1);
        
        // Course 2: 85% = B = 3.0 GPA
        Assignment assignment2 = new Assignment("Test2", 85.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Course 2", assignment2);
        
        // Act
        double gpa = validStudent.calculateGPA();
        
        // Assert
        // Average GPA: (4.0 + 3.0) / 2 = 3.5
        assertThat(gpa).isEqualTo(3.5);
    }

    // Transcript Generation Tests

    /**
     * Test transcript generation with enrolled courses.
     * Verifies that transcript contains all expected information.
     */
    @Test
    @DisplayName("Should generate comprehensive transcript")
    void shouldGenerateComprehensiveTranscript() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        validStudent.enrollInCourse(scienceCourse);
        
        Assignment mathAssignment = new Assignment("Math Test", 90.0, 100.0, GradingCategory.HOMEWORK);
        Assignment scienceAssignment = new Assignment("Science Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", mathAssignment);
        validStudent.addAssignment("Science 101", scienceAssignment);
        
        // Act
        String transcript = validStudent.generateTranscript();
        
        // Assert
        assertThat(transcript)
                .contains("STUDENT TRANSCRIPT")
                .contains("Student ID: " + VALID_STUDENT_ID)
                .contains("Name: " + VALID_STUDENT_NAME)
                .contains("Cumulative GPA:")
                .contains("Total Credit Hours: 7")
                .contains("Courses Completed:")
                .contains("Mathematics 101")
                .contains("Science 101")
                .contains("Academic Standing:");
    }

    /**
     * Test transcript generation with no courses.
     * Verifies that transcript handles empty course list correctly.
     */
    @Test
    @DisplayName("Should generate transcript with no courses")
    void shouldGenerateTranscriptWithNoCourses() {
        // Act
        String transcript = validStudent.generateTranscript();
        
        // Assert
        assertThat(transcript)
                .contains("STUDENT TRANSCRIPT")
                .contains("Student ID: " + VALID_STUDENT_ID)
                .contains("Name: " + VALID_STUDENT_NAME)
                .contains("Cumulative GPA: 0.00")
                .contains("Total Credit Hours: 0")
                .contains("No courses enrolled");
    }

    // Academic Standing Tests

    /**
     * Test academic standing determination based on GPA.
     * Verifies that academic standing is correctly determined for different GPA ranges.
     */
    @ParameterizedTest
    @CsvSource({
            "95.0, Dean's List",
            "90.0, Dean's List",
            "85.0, Good Standing",
            "80.0, Good Standing",
            "75.0, Satisfactory",
            "70.0, Satisfactory",
            "65.0, Academic Warning",
            "60.0, Academic Warning",
            "55.0, Academic Probation",
            "0.0, Academic Probation"
    })
    @DisplayName("Should determine correct academic standing")
    void shouldDetermineCorrectAcademicStanding(double percentage, String expectedStanding) {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment assignment = new Assignment("Test", percentage, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", assignment);
        
        // Act
        String standing = validStudent.getAcademicStanding();
        
        // Assert
        assertThat(standing).isEqualTo(expectedStanding);
    }

    /**
     * Test academic standing with no courses.
     * Verifies that academic probation is returned when no courses are enrolled.
     */
    @Test
    @DisplayName("Should return Academic Probation with no courses")
    void shouldReturnAcademicProbationWithNoCourses() {
        // Act
        String standing = validStudent.getAcademicStanding();
        
        // Assert
        assertThat(standing).isEqualTo("Academic Probation");
    }

    // Course Report Tests

    /**
     * Test course report generation.
     * Verifies that detailed course reports can be generated for enrolled courses.
     */
    @Test
    @DisplayName("Should generate course report for enrolled course")
    void shouldGenerateCourseReportForEnrolledCourse() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", assignment);
        
        // Act
        String report = validStudent.getCourseReport("Mathematics 101");
        
        // Assert
        assertThat(report)
                .contains("COURSE REPORT")
                .contains("Course: Mathematics 101")
                .contains("Credit Hours: 3")
                .contains("Final Grade:")
                .contains("Test");
    }

    /**
     * Test course report for non-enrolled course.
     * Verifies that IllegalArgumentException is thrown for non-enrolled course.
     */
    @Test
    @DisplayName("Should throw exception for course report of non-enrolled course")
    void shouldThrowExceptionForCourseReportOfNonEnrolledCourse() {
        // Act & Assert
        assertThatThrownBy(() -> validStudent.getCourseReport("Mathematics 101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Mathematics 101");
    }

    // Getter Tests

    /**
     * Test all getter methods return correct values.
     * Verifies that getters return the values set during construction and enrollment.
     */
    @Test
    @DisplayName("Should return correct values from getters")
    void shouldReturnCorrectValuesFromGetters() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        validStudent.enrollInCourse(scienceCourse);
        
        // Act & Assert
        assertThat(validStudent.getStudentId()).isEqualTo(VALID_STUDENT_ID);
        assertThat(validStudent.getName()).isEqualTo(VALID_STUDENT_NAME);
        assertThat(validStudent.getCourseCount()).isEqualTo(2);
        assertThat(validStudent.getCourses()).hasSize(2);
        assertThat(validStudent.getTotalCreditHours()).isEqualTo(7); // 3 + 4
    }

    /**
     * Test that getters return defensive copies.
     * Verifies that returned collections cannot modify internal state.
     */
    @Test
    @DisplayName("Should return defensive copies from getters")
    void shouldReturnDefensiveCopiesFromGetters() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        
        // Act
        List<Course> courses = validStudent.getCourses();
        
        // Assert - Modifying returned list should not affect student
        courses.clear();
        assertThat(validStudent.getCourseCount()).isEqualTo(1);
    }

    // Equals and HashCode Tests

    /**
     * Test equals method with identical student IDs.
     * Verifies that students with same ID are equal.
     */
    @Test
    @DisplayName("Should be equal when student IDs match")
    void shouldBeEqualWhenStudentIdsMatch() {
        // Arrange
        Student student1 = new Student("STU001", "John Doe");
        Student student2 = new Student("STU001", "Jane Smith"); // Different name
        
        // Act & Assert
        assertThat(student1).isEqualTo(student2);
        assertThat(student1.hashCode()).isEqualTo(student2.hashCode());
    }

    /**
     * Test equals method with different student IDs.
     * Verifies that students with different IDs are not equal.
     */
    @Test
    @DisplayName("Should not be equal when student IDs differ")
    void shouldNotBeEqualWhenStudentIdsDiffer() {
        // Arrange
        Student student1 = new Student("STU001", "John Doe");
        Student student2 = new Student("STU002", "John Doe"); // Same name
        
        // Act & Assert
        assertThat(student1).isNotEqualTo(student2);
    }

    /**
     * Test equals method with null object.
     * Verifies that student is not equal to null.
     */
    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Act & Assert
        assertThat(validStudent).isNotEqualTo(null);
    }

    /**
     * Test equals method reflexivity.
     * Verifies that a student equals itself.
     */
    @Test
    @DisplayName("Should be equal to itself (reflexivity)")
    void shouldBeEqualToItself() {
        // Act & Assert
        assertThat(validStudent).isEqualTo(validStudent);
    }

    // ToString Tests

    /**
     * Test toString method format and content.
     * Verifies that toString returns properly formatted string with student information.
     */
    @Test
    @DisplayName("Should return properly formatted toString")
    void shouldReturnProperlyFormattedToString() {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", assignment);
        
        // Act
        String result = validStudent.toString();
        
        // Assert
        assertThat(result)
                .contains("Student{")
                .contains("id='" + VALID_STUDENT_ID + "'")
                .contains("name='" + VALID_STUDENT_NAME + "'")
                .contains("courses=1")
                .contains("GPA=");
    }

    // CourseGrade Inner Class Tests

    /**
     * Test CourseGrade inner class creation and methods.
     * Verifies that CourseGrade correctly stores and returns percentage and letter grade.
     */
    @Test
    @DisplayName("Should create and use CourseGrade correctly")
    void shouldCreateAndUseCourseGradeCorrectly() {
        // Arrange
        double percentage = 87.5;
        String letterGrade = "B";
        
        // Act
        Student.CourseGrade courseGrade = new Student.CourseGrade(percentage, letterGrade);
        
        // Assert
        assertThat(courseGrade.getPercentage()).isEqualTo(percentage);
        assertThat(courseGrade.getLetterGrade()).isEqualTo(letterGrade);
        assertThat(courseGrade.toString()).isEqualTo("87.5% (B)");
    }

    // Edge Cases and Boundary Conditions

    /**
     * Test student with very long ID and name.
     * Verifies that long strings are handled correctly.
     */
    @Test
    @DisplayName("Should handle very long student ID and name")
    void shouldHandleVeryLongStudentIdAndName() {
        // Arrange
        String longId = "STU" + "0".repeat(1000);
        String longName = "A".repeat(1000);
        
        // Act
        Student student = new Student(longId, longName);
        
        // Assert
        assertThat(student.getStudentId()).hasSize(1003);
        assertThat(student.getName()).hasSize(1000);
    }

    /**
     * Test student with maximum number of courses.
     * Verifies that student can handle multiple course enrollments.
     */
    @Test
    @DisplayName("Should handle multiple course enrollments")
    void shouldHandleMultipleCourseEnrollments() {
        // Arrange & Act
        for (int i = 1; i <= 10; i++) {
            Course course = new Course("Course " + i, i);
            validStudent.enrollInCourse(course);
        }
        
        // Assert
        assertThat(validStudent.getCourseCount()).isEqualTo(10);
        assertThat(validStudent.getTotalCreditHours()).isEqualTo(55); // Sum of 1+2+...+10
    }

    /**
     * Test GPA calculation edge cases.
     * Verifies GPA calculation with boundary grade values.
     */
    @ParameterizedTest
    @ValueSource(doubles = {59.99, 60.0, 69.99, 70.0, 79.99, 80.0, 89.99, 90.0, 100.0})
    @DisplayName("Should handle GPA calculation edge cases")
    void shouldHandleGpaCalculationEdgeCases(double percentage) {
        // Arrange
        validStudent.enrollInCourse(mathCourse);
        Assignment assignment = new Assignment("Test", percentage, 100.0, GradingCategory.HOMEWORK);
        validStudent.addAssignment("Mathematics 101", assignment);
        
        // Act
        double gpa = validStudent.calculateGPA();
        
        // Assert
        assertThat(gpa).isBetween(0.0, 4.0);
    }
}