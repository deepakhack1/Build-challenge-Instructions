package com.challenge.gradebook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main gradebook class that manages multiple students and provides gradebook operations.
 * Handles student management, course enrollment, and grade reporting functionality.
 */
public class GradeBook {
    private final Map<String, Student> students;
    
    /**
     * Constructor initializes the gradebook with an empty students map
     */
    public GradeBook() {
        this.students = new HashMap<>();
    }
    
    /**
     * Add a new student to the gradebook
     * 
     * @param studentId Unique student identifier
     * @param name Student's full name
     * @throws IllegalArgumentException if student already exists or parameters are invalid
     */
    public void addStudent(String studentId, String name) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        
        String trimmedId = studentId.trim();
        if (students.containsKey(trimmedId)) {
            throw new IllegalArgumentException("Student with ID " + trimmedId + " already exists");
        }
        
        Student student = new Student(trimmedId, name);
        students.put(trimmedId, student);
    }
    
    /**
     * Get a student by ID
     * 
     * @param studentId Student ID to retrieve
     * @return Student object
     * @throws IllegalArgumentException if student doesn't exist
     */
    private Student getStudent(String studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        return student;
    }
    
    /**
     * Enroll a student in a course
     * 
     * @param studentId Student ID
     * @param courseName Name of the course
     * @param creditHours Credit hours for the course
     * @throws IllegalArgumentException if student doesn't exist or parameters are invalid
     */
    public void enrollInCourse(String studentId, String courseName, int creditHours) {
        Student student = getStudent(studentId);
        
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        if (creditHours <= 0) {
            throw new IllegalArgumentException("Credit hours must be positive");
        }
        
        // Check if student is already enrolled in this course
        if (student.isEnrolledIn(courseName)) {
            throw new IllegalArgumentException("Student " + studentId + " is already enrolled in " + courseName);
        }
        
        Course course = new Course(courseName, creditHours);
        student.enrollInCourse(course);
    }
    
    /**
     * Enroll a student in a course with custom category weights
     * 
     * @param studentId Student ID
     * @param courseName Name of the course
     * @param creditHours Credit hours for the course
     * @param categoryWeights Custom category weights (must total 100%)
     * @throws IllegalArgumentException if student doesn't exist or parameters are invalid
     */
    public void enrollInCourse(String studentId, String courseName, int creditHours, Map<GradingCategory, Double> categoryWeights) {
        Student student = getStudent(studentId);
        
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        if (creditHours <= 0) {
            throw new IllegalArgumentException("Credit hours must be positive");
        }
        
        // Check if student is already enrolled in this course
        if (student.isEnrolledIn(courseName)) {
            throw new IllegalArgumentException("Student " + studentId + " is already enrolled in " + courseName);
        }
        
        Course course = new Course(courseName, creditHours, categoryWeights);
        student.enrollInCourse(course);
    }
    
    /**
     * Add an assignment to a student's course
     * 
     * @param studentId Student ID
     * @param courseName Course name
     * @param assignment Assignment to add
     * @throws IllegalArgumentException if student doesn't exist, not enrolled in course, or assignment is invalid
     */
    public void addAssignment(String studentId, String courseName, Assignment assignment) {
        Student student = getStudent(studentId);
        
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null");
        }
        
        student.addAssignment(courseName, assignment);
    }
    
    /**
     * Get category average for a student's course
     * 
     * @param studentId Student ID
     * @param courseName Course name
     * @param category Grading category
     * @return Category average percentage
     * @throws IllegalArgumentException if student doesn't exist or not enrolled in course
     */
    public double getCategoryAverage(String studentId, String courseName, GradingCategory category) {
        Student student = getStudent(studentId);
        return student.getCategoryAverage(courseName, category);
    }
    
    /**
     * Get course grade for a student
     * 
     * @param studentId Student ID
     * @param courseName Course name
     * @return CourseGrade object with percentage and letter grade
     * @throws IllegalArgumentException if student doesn't exist or not enrolled in course
     */
    public Student.CourseGrade getCourseGrade(String studentId, String courseName) {
        Student student = getStudent(studentId);
        return student.getCourseGrade(courseName);
    }
    
    /**
     * Calculate GPA for a student
     * 
     * @param studentId Student ID
     * @return Cumulative GPA weighted by credit hours
     * @throws IllegalArgumentException if student doesn't exist
     */
    public double calculateGPA(String studentId) {
        Student student = getStudent(studentId);
        return student.calculateGPA();
    }
    
    /**
     * Generate transcript for a student
     * 
     * @param studentId Student ID
     * @return Formatted transcript string
     * @throws IllegalArgumentException if student doesn't exist
     */
    public String generateTranscript(String studentId) {
        Student student = getStudent(studentId);
        return student.generateTranscript();
    }
    
    /**
     * Get detailed course report for a student
     * 
     * @param studentId Student ID
     * @param courseName Course name
     * @return Detailed course report
     * @throws IllegalArgumentException if student doesn't exist or not enrolled in course
     */
    public String getCourseReport(String studentId, String courseName) {
        Student student = getStudent(studentId);
        return student.getCourseReport(courseName);
    }
    
    /**
     * Get student information
     * 
     * @param studentId Student ID
     * @return Student object
     * @throws IllegalArgumentException if student doesn't exist
     */
    public Student getStudentInfo(String studentId) {
        return getStudent(studentId);
    }
    
    /**
     * Check if a student exists in the gradebook
     * 
     * @param studentId Student ID to check
     * @return true if student exists, false otherwise
     */
    public boolean hasStudent(String studentId) {
        return students.containsKey(studentId);
    }
    
    /**
     * Remove a student from the gradebook
     * 
     * @param studentId Student ID to remove
     * @throws IllegalArgumentException if student doesn't exist
     */
    public void removeStudent(String studentId) {
        if (!students.containsKey(studentId)) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        students.remove(studentId);
    }
    
    /**
     * Get all students (for administrative purposes)
     * 
     * @return Map of all students
     */
    public Map<String, Student> getAllStudents() {
        return new HashMap<>(students);
    }
    
    /**
     * Get total number of students
     * 
     * @return Number of students
     */
    public int getStudentCount() {
        return students.size();
    }
    
    /**
     * Get students enrolled in a specific course
     * 
     * @param courseName Course name
     * @return List of students enrolled in the course
     */
    public List<Student> getStudentsInCourse(String courseName) {
        return students.values().stream()
                .filter(student -> student.isEnrolledIn(courseName))
                .collect(Collectors.toList());
    }
    
    /**
     * Generate class roster for a course
     * 
     * @param courseName Course name
     * @return Formatted class roster
     */
    public String generateClassRoster(String courseName) {
        List<Student> enrolledStudents = getStudentsInCourse(courseName);
        
        StringBuilder roster = new StringBuilder();
        roster.append("\n=== CLASS ROSTER ===").append("\n");
        roster.append("Course: ").append(courseName).append("\n");
        roster.append("Enrolled Students: ").append(enrolledStudents.size()).append("\n\n");
        
        if (enrolledStudents.isEmpty()) {
            roster.append("No students enrolled\n");
        } else {
            roster.append(String.format("%-12s %-20s %8s %5s\n", "Student ID", "Name", "Grade", "GPA"));
            roster.append("-".repeat(50)).append("\n");
            
            for (Student student : enrolledStudents) {
                try {
                    Student.CourseGrade grade = student.getCourseGrade(courseName);
                    roster.append(String.format("%-12s %-20s %7.1f%% %5.2f\n",
                            student.getStudentId(),
                            student.getName(),
                            grade.getPercentage(),
                            student.calculateGPA()));
                } catch (Exception e) {
                    roster.append(String.format("%-12s %-20s %8s %5s\n",
                            student.getStudentId(),
                            student.getName(),
                            "N/A",
                            String.format("%.2f", student.calculateGPA())));
                }
            }
        }
        
        return roster.toString();
    }
    
    /**
     * Generate gradebook summary report
     * 
     * @return Summary of all students and their performance
     */
    public String generateSummaryReport() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n=== GRADEBOOK SUMMARY ===").append("\n");
        summary.append("Total Students: ").append(students.size()).append("\n");
        
        if (students.isEmpty()) {
            summary.append("No students in gradebook\n");
            return summary.toString();
        }
        
        double totalGPA = 0.0;
        int totalCreditHours = 0;
        
        summary.append("\nStudent Performance:\n");
        summary.append(String.format("%-12s %-20s %8s %8s %12s\n", 
                "Student ID", "Name", "GPA", "Credits", "Standing"));
        summary.append("-".repeat(65)).append("\n");
        
        for (Student student : students.values()) {
            double gpa = student.calculateGPA();
            int credits = student.getTotalCreditHours();
            
            totalGPA += gpa;
            totalCreditHours += credits;
            
            summary.append(String.format("%-12s %-20s %8.2f %8d %12s\n",
                    student.getStudentId(),
                    student.getName(),
                    gpa,
                    credits,
                    student.getAcademicStanding()));
        }
        
        double averageGPA = totalGPA / students.size();
        double averageCredits = (double) totalCreditHours / students.size();
        
        summary.append("\nOverall Statistics:\n");
        summary.append(String.format("Average GPA: %.2f\n", averageGPA));
        summary.append(String.format("Average Credit Hours: %.1f\n", averageCredits));
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return String.format("GradeBook{students=%d}", students.size());
    }
}