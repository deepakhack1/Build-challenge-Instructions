package com.challenge.gradebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the GradeBook class.
 * Tests student management, course enrollment, assignment operations, and report generation.
 */
@DisplayName("GradeBook Class Tests")
class GradeBookTest {

    private GradeBook gradeBook;
    private static final String STUDENT_ID_1 = "STU001";
    private static final String STUDENT_NAME_1 = "John Doe";
    private static final String STUDENT_ID_2 = "STU002";
    private static final String STUDENT_NAME_2 = "Jane Smith";
    private static final String COURSE_NAME_1 = "Mathematics 101";
    private static final String COURSE_NAME_2 = "Science 101";

    @BeforeEach
    void setUp() {
        gradeBook = new GradeBook();
    }

    // Student Management Tests

    /**
     * Test successful student addition to gradebook.
     * Verifies that students can be added with valid parameters.
     */
    @Test
    @DisplayName("Should add student successfully")
    void shouldAddStudentSuccessfully() {
        // Act
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Assert
        assertThat(gradeBook.getStudentCount()).isEqualTo(1);
        assertThat(gradeBook.hasStudent(STUDENT_ID_1)).isTrue();
        
        Student student = gradeBook.getStudentInfo(STUDENT_ID_1);
        assertThat(student.getStudentId()).isEqualTo(STUDENT_ID_1);
        assertThat(student.getName()).isEqualTo(STUDENT_NAME_1);
    }

    /**
     * Test adding multiple students to gradebook.
     * Verifies that multiple students can be managed simultaneously.
     */
    @Test
    @DisplayName("Should add multiple students successfully")
    void shouldAddMultipleStudentsSuccessfully() {
        // Act
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        
        // Assert
        assertThat(gradeBook.getStudentCount()).isEqualTo(2);
        assertThat(gradeBook.hasStudent(STUDENT_ID_1)).isTrue();
        assertThat(gradeBook.hasStudent(STUDENT_ID_2)).isTrue();
        
        Map<String, Student> allStudents = gradeBook.getAllStudents();
        assertThat(allStudents).hasSize(2);
        assertThat(allStudents).containsKeys(STUDENT_ID_1, STUDENT_ID_2);
    }

    /**
     * Test adding student with trimmed whitespace.
     * Verifies that leading and trailing whitespace is handled correctly.
     */
    @Test
    @DisplayName("Should trim whitespace when adding student")
    void shouldTrimWhitespaceWhenAddingStudent() {
        // Arrange
        String idWithWhitespace = "  STU003  ";
        String nameWithWhitespace = "  Bob Johnson  ";
        
        // Act
        gradeBook.addStudent(idWithWhitespace, nameWithWhitespace);
        
        // Assert
        assertThat(gradeBook.hasStudent("STU003")).isTrue();
        Student student = gradeBook.getStudentInfo("STU003");
        assertThat(student.getName()).isEqualTo("Bob Johnson");
    }

    /**
     * Test adding student with null ID.
     * Verifies that IllegalArgumentException is thrown for null student ID.
     */
    @Test
    @DisplayName("Should throw exception for null student ID")
    void shouldThrowExceptionForNullStudentId() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addStudent(null, STUDENT_NAME_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID cannot be null or empty");
    }

    /**
     * Test adding student with empty ID.
     * Verifies that IllegalArgumentException is thrown for empty student ID.
     */
    @Test
    @DisplayName("Should throw exception for empty student ID")
    void shouldThrowExceptionForEmptyStudentId() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addStudent("", STUDENT_NAME_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID cannot be null or empty");
    }

    /**
     * Test adding student with whitespace-only ID.
     * Verifies that IllegalArgumentException is thrown for whitespace-only student ID.
     */
    @Test
    @DisplayName("Should throw exception for whitespace-only student ID")
    void shouldThrowExceptionForWhitespaceOnlyStudentId() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addStudent("   ", STUDENT_NAME_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID cannot be null or empty");
    }

    /**
     * Test adding student with null name.
     * Verifies that IllegalArgumentException is thrown for null student name.
     */
    @Test
    @DisplayName("Should throw exception for null student name")
    void shouldThrowExceptionForNullStudentName() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addStudent(STUDENT_ID_1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student name cannot be null or empty");
    }

    /**
     * Test adding student with empty name.
     * Verifies that IllegalArgumentException is thrown for empty student name.
     */
    @Test
    @DisplayName("Should throw exception for empty student name")
    void shouldThrowExceptionForEmptyStudentName() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addStudent(STUDENT_ID_1, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student name cannot be null or empty");
    }

    /**
     * Test adding duplicate student.
     * Verifies that IllegalArgumentException is thrown for duplicate student ID.
     */
    @Test
    @DisplayName("Should throw exception for duplicate student ID")
    void shouldThrowExceptionForDuplicateStudentId() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addStudent(STUDENT_ID_1, "Different Name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student with ID " + STUDENT_ID_1 + " already exists");
    }

    /**
     * Test removing student from gradebook.
     * Verifies that students can be removed successfully.
     */
    @Test
    @DisplayName("Should remove student successfully")
    void shouldRemoveStudentSuccessfully() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        
        // Act
        gradeBook.removeStudent(STUDENT_ID_1);
        
        // Assert
        assertThat(gradeBook.getStudentCount()).isEqualTo(1);
        assertThat(gradeBook.hasStudent(STUDENT_ID_1)).isFalse();
        assertThat(gradeBook.hasStudent(STUDENT_ID_2)).isTrue();
    }

    /**
     * Test removing non-existent student.
     * Verifies that IllegalArgumentException is thrown for non-existent student.
     */
    @Test
    @DisplayName("Should throw exception when removing non-existent student")
    void shouldThrowExceptionWhenRemovingNonExistentStudent() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.removeStudent("NONEXISTENT"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: NONEXISTENT");
    }

    /**
     * Test getting student info for non-existent student.
     * Verifies that IllegalArgumentException is thrown for non-existent student.
     */
    @Test
    @DisplayName("Should throw exception when getting info for non-existent student")
    void shouldThrowExceptionWhenGettingInfoForNonExistentStudent() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.getStudentInfo("NONEXISTENT"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: NONEXISTENT");
    }

    // Course Enrollment Tests

    /**
     * Test successful course enrollment with default weights.
     * Verifies that students can be enrolled in courses with default category weights.
     */
    @Test
    @DisplayName("Should enroll student in course with default weights")
    void shouldEnrollStudentInCourseWithDefaultWeights() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        
        // Assert
        Student student = gradeBook.getStudentInfo(STUDENT_ID_1);
        assertThat(student.isEnrolledIn(COURSE_NAME_1)).isTrue();
        assertThat(student.getCourseCount()).isEqualTo(1);
    }

    /**
     * Test successful course enrollment with custom weights.
     * Verifies that students can be enrolled in courses with custom category weights.
     */
    @Test
    @DisplayName("Should enroll student in course with custom weights")
    void shouldEnrollStudentInCourseWithCustomWeights() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        Map<GradingCategory, Double> customWeights = new EnumMap<>(GradingCategory.class);
        customWeights.put(GradingCategory.HOMEWORK, 30.0);
        customWeights.put(GradingCategory.QUIZZES, 20.0);
        customWeights.put(GradingCategory.MIDTERM, 20.0);
        customWeights.put(GradingCategory.FINAL_EXAM, 30.0);
        
        // Act
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3, customWeights);
        
        // Assert
        Student student = gradeBook.getStudentInfo(STUDENT_ID_1);
        assertThat(student.isEnrolledIn(COURSE_NAME_1)).isTrue();
        
        Course course = student.getCourse(COURSE_NAME_1);
        Map<GradingCategory, Double> weights = course.getCategoryWeights();
        assertThat(weights.get(GradingCategory.HOMEWORK)).isEqualTo(30.0);
    }

    /**
     * Test enrolling multiple students in same course.
     * Verifies that multiple students can be enrolled in the same course.
     */
    @Test
    @DisplayName("Should enroll multiple students in same course")
    void shouldEnrollMultipleStudentsInSameCourse() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        
        // Act
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_2, COURSE_NAME_1, 3);
        
        // Assert
        List<Student> enrolledStudents = gradeBook.getStudentsInCourse(COURSE_NAME_1);
        assertThat(enrolledStudents).hasSize(2);
        assertThat(enrolledStudents.stream().map(Student::getStudentId))
                .containsExactlyInAnyOrder(STUDENT_ID_1, STUDENT_ID_2);
    }

    /**
     * Test enrolling student in multiple courses.
     * Verifies that a student can be enrolled in multiple courses.
     */
    @Test
    @DisplayName("Should enroll student in multiple courses")
    void shouldEnrollStudentInMultipleCourses() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_2, 4);
        
        // Assert
        Student student = gradeBook.getStudentInfo(STUDENT_ID_1);
        assertThat(student.getCourseCount()).isEqualTo(2);
        assertThat(student.isEnrolledIn(COURSE_NAME_1)).isTrue();
        assertThat(student.isEnrolledIn(COURSE_NAME_2)).isTrue();
    }

    /**
     * Test enrolling non-existent student in course.
     * Verifies that IllegalArgumentException is thrown for non-existent student.
     */
    @Test
    @DisplayName("Should throw exception when enrolling non-existent student")
    void shouldThrowExceptionWhenEnrollingNonExistentStudent() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.enrollInCourse("NONEXISTENT", COURSE_NAME_1, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: NONEXISTENT");
    }

    /**
     * Test enrolling student with null course name.
     * Verifies that IllegalArgumentException is thrown for null course name.
     */
    @Test
    @DisplayName("Should throw exception for null course name")
    void shouldThrowExceptionForNullCourseName() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.enrollInCourse(STUDENT_ID_1, null, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course name cannot be null or empty");
    }

    /**
     * Test enrolling student with empty course name.
     * Verifies that IllegalArgumentException is thrown for empty course name.
     */
    @Test
    @DisplayName("Should throw exception for empty course name")
    void shouldThrowExceptionForEmptyCourseName() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.enrollInCourse(STUDENT_ID_1, "", 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course name cannot be null or empty");
    }

    /**
     * Test enrolling student with invalid credit hours.
     * Verifies that IllegalArgumentException is thrown for invalid credit hours.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("Should throw exception for invalid credit hours")
    void shouldThrowExceptionForInvalidCreditHours(int creditHours) {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, creditHours))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credit hours must be positive");
    }

    /**
     * Test duplicate course enrollment.
     * Verifies that IllegalArgumentException is thrown for duplicate enrollment.
     */
    @Test
    @DisplayName("Should throw exception for duplicate course enrollment")
    void shouldThrowExceptionForDuplicateCourseEnrollment() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student " + STUDENT_ID_1 + " is already enrolled in " + COURSE_NAME_1);
    }

    // Assignment Operations Tests

    /**
     * Test adding assignment to student's course.
     * Verifies that assignments can be added to enrolled courses.
     */
    @Test
    @DisplayName("Should add assignment to student's course")
    void shouldAddAssignmentToStudentCourse() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        Assignment assignment = new Assignment("Homework 1", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment);
        
        // Assert
        Student student = gradeBook.getStudentInfo(STUDENT_ID_1);
        Course course = student.getCourse(COURSE_NAME_1);
        assertThat(course.getAssignmentCount()).isEqualTo(1);
        assertThat(course.getAssignments()).contains(assignment);
    }

    /**
     * Test adding assignment for non-existent student.
     * Verifies that IllegalArgumentException is thrown for non-existent student.
     */
    @Test
    @DisplayName("Should throw exception when adding assignment for non-existent student")
    void shouldThrowExceptionWhenAddingAssignmentForNonExistentStudent() {
        // Arrange
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addAssignment("NONEXISTENT", COURSE_NAME_1, assignment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: NONEXISTENT");
    }

    /**
     * Test adding null assignment.
     * Verifies that IllegalArgumentException is thrown for null assignment.
     */
    @Test
    @DisplayName("Should throw exception for null assignment")
    void shouldThrowExceptionForNullAssignment() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assignment cannot be null");
    }

    /**
     * Test adding assignment to non-enrolled course.
     * Verifies that IllegalArgumentException is thrown when student is not enrolled in course.
     */
    @Test
    @DisplayName("Should throw exception when adding assignment to non-enrolled course")
    void shouldThrowExceptionWhenAddingAssignmentToNonEnrolledCourse() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: " + COURSE_NAME_1);
    }

    // Grade Retrieval and Calculation Tests

    /**
     * Test getting category average for student's course.
     * Verifies that category averages can be retrieved correctly.
     */
    @Test
    @DisplayName("Should get category average for student's course")
    void shouldGetCategoryAverageForStudentCourse() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        Assignment hw1 = new Assignment("HW1", 80.0, 100.0, GradingCategory.HOMEWORK);
        Assignment hw2 = new Assignment("HW2", 90.0, 100.0, GradingCategory.HOMEWORK);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, hw1);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, hw2);
        
        // Act
        double average = gradeBook.getCategoryAverage(STUDENT_ID_1, COURSE_NAME_1, GradingCategory.HOMEWORK);
        
        // Assert
        assertThat(average).isEqualTo(85.0); // (80 + 90) / 2
    }

    /**
     * Test getting category average for non-existent student.
     * Verifies that IllegalArgumentException is thrown for non-existent student.
     */
    @Test
    @DisplayName("Should throw exception when getting category average for non-existent student")
    void shouldThrowExceptionWhenGettingCategoryAverageForNonExistentStudent() {
        // Act & Assert
        assertThatThrownBy(() -> gradeBook.getCategoryAverage("NONEXISTENT", COURSE_NAME_1, GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: NONEXISTENT");
    }

    /**
     * Test getting course grade for student.
     * Verifies that course grades can be retrieved correctly.
     */
    @Test
    @DisplayName("Should get course grade for student")
    void shouldGetCourseGradeForStudent() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment);
        
        // Act
        Student.CourseGrade grade = gradeBook.getCourseGrade(STUDENT_ID_1, COURSE_NAME_1);
        
        // Assert
        assertThat(grade.getPercentage()).isEqualTo(85.0);
        assertThat(grade.getLetterGrade()).isEqualTo("B");
    }

    /**
     * Test calculating GPA for student.
     * Verifies that student GPA can be calculated correctly.
     */
    @Test
    @DisplayName("Should calculate GPA for student")
    void shouldCalculateGpaForStudent() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_2, 3);
        
        Assignment math = new Assignment("Math Test", 90.0, 100.0, GradingCategory.HOMEWORK); // A = 4.0
        Assignment science = new Assignment("Science Test", 80.0, 100.0, GradingCategory.HOMEWORK); // B = 3.0
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, math);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_2, science);
        
        // Act
        double gpa = gradeBook.calculateGPA(STUDENT_ID_1);
        
        // Assert
        assertThat(gpa).isEqualTo(3.5); // (4.0 + 3.0) / 2
    }

    // Transcript and Report Generation Tests

    /**
     * Test generating transcript for student.
     * Verifies that comprehensive transcripts can be generated.
     */
    @Test
    @DisplayName("Should generate transcript for student")
    void shouldGenerateTranscriptForStudent() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment);
        
        // Act
        String transcript = gradeBook.generateTranscript(STUDENT_ID_1);
        
        // Assert
        assertThat(transcript)
                .contains("STUDENT TRANSCRIPT")
                .contains("Student ID: " + STUDENT_ID_1)
                .contains("Name: " + STUDENT_NAME_1)
                .contains(COURSE_NAME_1)
                .contains("Cumulative GPA:");
    }

    /**
     * Test generating course report for student.
     * Verifies that detailed course reports can be generated.
     */
    @Test
    @DisplayName("Should generate course report for student")
    void shouldGenerateCourseReportForStudent() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment);
        
        // Act
        String report = gradeBook.getCourseReport(STUDENT_ID_1, COURSE_NAME_1);
        
        // Assert
        assertThat(report)
                .contains("COURSE REPORT")
                .contains("Course: " + COURSE_NAME_1)
                .contains("Test");
    }

    /**
     * Test generating class roster for course.
     * Verifies that class rosters can be generated with student information.
     */
    @Test
    @DisplayName("Should generate class roster for course")
    void shouldGenerateClassRosterForCourse() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_2, COURSE_NAME_1, 3);
        
        Assignment assignment1 = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment assignment2 = new Assignment("Test", 90.0, 100.0, GradingCategory.HOMEWORK);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment1);
        gradeBook.addAssignment(STUDENT_ID_2, COURSE_NAME_1, assignment2);
        
        // Act
        String roster = gradeBook.generateClassRoster(COURSE_NAME_1);
        
        // Assert
        assertThat(roster)
                .contains("CLASS ROSTER")
                .contains("Course: " + COURSE_NAME_1)
                .contains("Enrolled Students: 2")
                .contains(STUDENT_ID_1)
                .contains(STUDENT_ID_2)
                .contains(STUDENT_NAME_1)
                .contains(STUDENT_NAME_2);
    }

    /**
     * Test generating class roster for course with no students.
     * Verifies that empty roster is handled correctly.
     */
    @Test
    @DisplayName("Should generate empty class roster")
    void shouldGenerateEmptyClassRoster() {
        // Act
        String roster = gradeBook.generateClassRoster("Empty Course");
        
        // Assert
        assertThat(roster)
                .contains("CLASS ROSTER")
                .contains("Course: Empty Course")
                .contains("Enrolled Students: 0")
                .contains("No students enrolled");
    }

    /**
     * Test generating summary report for gradebook.
     * Verifies that comprehensive summary reports can be generated.
     */
    @Test
    @DisplayName("Should generate summary report for gradebook")
    void shouldGenerateSummaryReportForGradebook() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_2, COURSE_NAME_2, 4);
        
        Assignment assignment1 = new Assignment("Test1", 90.0, 100.0, GradingCategory.HOMEWORK); // A
        Assignment assignment2 = new Assignment("Test2", 80.0, 100.0, GradingCategory.HOMEWORK); // B
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, assignment1);
        gradeBook.addAssignment(STUDENT_ID_2, COURSE_NAME_2, assignment2);
        
        // Act
        String summary = gradeBook.generateSummaryReport();
        
        // Assert
        assertThat(summary)
                .contains("GRADEBOOK SUMMARY")
                .contains("Total Students: 2")
                .contains("Student Performance:")
                .contains(STUDENT_ID_1)
                .contains(STUDENT_ID_2)
                .contains(STUDENT_NAME_1)
                .contains(STUDENT_NAME_2)
                .contains("Overall Statistics:")
                .contains("Average GPA:");
    }

    /**
     * Test generating summary report for empty gradebook.
     * Verifies that empty gradebook summary is handled correctly.
     */
    @Test
    @DisplayName("Should generate summary report for empty gradebook")
    void shouldGenerateSummaryReportForEmptyGradebook() {
        // Act
        String summary = gradeBook.generateSummaryReport();
        
        // Assert
        assertThat(summary)
                .contains("GRADEBOOK SUMMARY")
                .contains("Total Students: 0")
                .contains("No students in gradebook");
    }

    // Error Handling Tests

    /**
     * Test error handling for operations on non-existent students.
     * Verifies that appropriate exceptions are thrown for all operations involving non-existent students.
     */
    @Test
    @DisplayName("Should handle operations on non-existent students")
    void shouldHandleOperationsOnNonExistentStudents() {
        // Test various operations that should fail for non-existent students
        String nonExistentId = "NONEXISTENT";
        
        assertThatThrownBy(() -> gradeBook.getStudentInfo(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: " + nonExistentId);
        
        assertThatThrownBy(() -> gradeBook.enrollInCourse(nonExistentId, COURSE_NAME_1, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: " + nonExistentId);
        
        assertThatThrownBy(() -> gradeBook.calculateGPA(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: " + nonExistentId);
        
        assertThatThrownBy(() -> gradeBook.generateTranscript(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found: " + nonExistentId);
    }

    /**
     * Test error handling for operations on non-enrolled courses.
     * Verifies that appropriate exceptions are thrown for course operations when student is not enrolled.
     */
    @Test
    @DisplayName("Should handle operations on non-enrolled courses")
    void shouldHandleOperationsOnNonEnrolledCourses() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        Assignment assignment = new Assignment("Test", 85.0, 100.0, GradingCategory.HOMEWORK);
        
        // Test operations that should fail for non-enrolled courses
        assertThatThrownBy(() -> gradeBook.addAssignment(STUDENT_ID_1, "Non-enrolled Course", assignment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Non-enrolled Course");
        
        assertThatThrownBy(() -> gradeBook.getCategoryAverage(STUDENT_ID_1, "Non-enrolled Course", GradingCategory.HOMEWORK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Non-enrolled Course");
        
        assertThatThrownBy(() -> gradeBook.getCourseGrade(STUDENT_ID_1, "Non-enrolled Course"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Non-enrolled Course");
        
        assertThatThrownBy(() -> gradeBook.getCourseReport(STUDENT_ID_1, "Non-enrolled Course"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student is not enrolled in course: Non-enrolled Course");
    }

    // Getter and Utility Method Tests

    /**
     * Test getAllStudents method returns defensive copy.
     * Verifies that returned map cannot modify internal state.
     */
    @Test
    @DisplayName("Should return defensive copy from getAllStudents")
    void shouldReturnDefensiveCopyFromGetAllStudents() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act
        Map<String, Student> allStudents = gradeBook.getAllStudents();
        
        // Assert - Modifying returned map should not affect gradebook
        allStudents.clear();
        assertThat(gradeBook.getStudentCount()).isEqualTo(1);
    }

    /**
     * Test getStudentsInCourse method.
     * Verifies that students enrolled in a course can be retrieved correctly.
     */
    @Test
    @DisplayName("Should get students in course correctly")
    void shouldGetStudentsInCourseCorrectly() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_2, COURSE_NAME_1, 3);
        
        // Act
        List<Student> studentsInCourse = gradeBook.getStudentsInCourse(COURSE_NAME_1);
        List<Student> studentsInOtherCourse = gradeBook.getStudentsInCourse("Other Course");
        
        // Assert
        assertThat(studentsInCourse).hasSize(2);
        assertThat(studentsInCourse.stream().map(Student::getStudentId))
                .containsExactlyInAnyOrder(STUDENT_ID_1, STUDENT_ID_2);
        assertThat(studentsInOtherCourse).isEmpty();
    }

    /**
     * Test hasStudent method.
     * Verifies that student existence can be checked correctly.
     */
    @Test
    @DisplayName("Should check student existence correctly")
    void shouldCheckStudentExistenceCorrectly() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        
        // Act & Assert
        assertThat(gradeBook.hasStudent(STUDENT_ID_1)).isTrue();
        assertThat(gradeBook.hasStudent("NONEXISTENT")).isFalse();
        assertThat(gradeBook.hasStudent(STUDENT_ID_2)).isFalse();
    }

    // ToString Test

    /**
     * Test toString method format and content.
     * Verifies that toString returns properly formatted string with gradebook information.
     */
    @Test
    @DisplayName("Should return properly formatted toString")
    void shouldReturnProperlyFormattedToString() {
        // Arrange
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        
        // Act
        String result = gradeBook.toString();
        
        // Assert
        assertThat(result)
                .contains("GradeBook{")
                .contains("students=2");
    }

    // Edge Cases and Boundary Conditions

    /**
     * Test gradebook with maximum number of students.
     * Verifies that gradebook can handle large numbers of students.
     */
    @Test
    @DisplayName("Should handle large number of students")
    void shouldHandleLargeNumberOfStudents() {
        // Arrange & Act
        for (int i = 1; i <= 100; i++) {
            gradeBook.addStudent("STU" + String.format("%03d", i), "Student " + i);
        }
        
        // Assert
        assertThat(gradeBook.getStudentCount()).isEqualTo(100);
        assertThat(gradeBook.getAllStudents()).hasSize(100);
    }

    /**
     * Test gradebook operations with very long names.
     * Verifies that long strings are handled correctly.
     */
    @Test
    @DisplayName("Should handle very long student and course names")
    void shouldHandleVeryLongStudentAndCourseNames() {
        // Arrange
        String longStudentId = "STU" + "0".repeat(100);
        String longStudentName = "A".repeat(1000);
        String longCourseName = "B".repeat(1000);
        
        // Act
        gradeBook.addStudent(longStudentId, longStudentName);
        gradeBook.enrollInCourse(longStudentId, longCourseName, 3);
        
        // Assert
        assertThat(gradeBook.hasStudent(longStudentId)).isTrue();
        Student student = gradeBook.getStudentInfo(longStudentId);
        assertThat(student.isEnrolledIn(longCourseName)).isTrue();
    }

    /**
     * Test complex gradebook scenario with multiple students, courses, and assignments.
     * Verifies that complex scenarios work correctly with all operations.
     */
    @Test
    @DisplayName("Should handle complex gradebook scenario")
    void shouldHandleComplexGradebookScenario() {
        // Arrange - Create complex scenario
        gradeBook.addStudent(STUDENT_ID_1, STUDENT_NAME_1);
        gradeBook.addStudent(STUDENT_ID_2, STUDENT_NAME_2);
        
        // Enroll students in multiple courses
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_1, 3);
        gradeBook.enrollInCourse(STUDENT_ID_1, COURSE_NAME_2, 4);
        gradeBook.enrollInCourse(STUDENT_ID_2, COURSE_NAME_1, 3);
        
        // Add multiple assignments
        Assignment hw1 = new Assignment("HW1", 85.0, 100.0, GradingCategory.HOMEWORK);
        Assignment quiz1 = new Assignment("Quiz1", 90.0, 100.0, GradingCategory.QUIZZES);
        Assignment midterm1 = new Assignment("Midterm1", 88.0, 100.0, GradingCategory.MIDTERM);
        
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, hw1);
        gradeBook.addAssignment(STUDENT_ID_1, COURSE_NAME_1, quiz1);
        gradeBook.addAssignment(STUDENT_ID_2, COURSE_NAME_1, midterm1);
        
        // Act & Assert - Verify all operations work correctly
        assertThat(gradeBook.getStudentCount()).isEqualTo(2);
        assertThat(gradeBook.getStudentsInCourse(COURSE_NAME_1)).hasSize(2);
        assertThat(gradeBook.getStudentsInCourse(COURSE_NAME_2)).hasSize(1);
        
        double gpa1 = gradeBook.calculateGPA(STUDENT_ID_1);
        double gpa2 = gradeBook.calculateGPA(STUDENT_ID_2);
        assertThat(gpa1).isGreaterThan(0.0);
        assertThat(gpa2).isGreaterThan(0.0);
        
        String summary = gradeBook.generateSummaryReport();
        assertThat(summary).contains("Total Students: 2");
    }
}