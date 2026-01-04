package com.challenge.gradebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student with ID, name, and enrolled courses.
 * Manages student-level operations and course enrollment.
 */
public class Student {
    private final String studentId;
    private final String name;
    private final List<Course> courses;
    
    /**
     * Constructor for creating a student
     * 
     * @param studentId Unique student identifier
     * @param name Student's full name
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Student(String studentId, String name) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        
        this.studentId = studentId.trim();
        this.name = name.trim();
        this.courses = new ArrayList<>();
    }
    
    /**
     * Enroll the student in a course
     * 
     * @param course Course to enroll in
     * @throws IllegalArgumentException if course is null or already enrolled
     */
    public void enrollInCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        
        // Check if already enrolled
        if (courses.contains(course)) {
            throw new IllegalArgumentException("Student is already enrolled in course: " + course.getCourseName());
        }
        
        courses.add(course);
    }
    
    /**
     * Get a course by name
     * 
     * @param courseName Name of the course
     * @return Course object if found, null otherwise
     */
    public Course getCourse(String courseName) {
        return courses.stream()
                .filter(course -> course.getCourseName().equalsIgnoreCase(courseName))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Check if student is enrolled in a specific course
     * 
     * @param courseName Name of the course
     * @return true if enrolled, false otherwise
     */
    public boolean isEnrolledIn(String courseName) {
        return getCourse(courseName) != null;
    }
    
    /**
     * Add an assignment to a specific course
     * 
     * @param courseName Name of the course
     * @param assignment Assignment to add
     * @throws IllegalArgumentException if course not found or assignment is null
     */
    public void addAssignment(String courseName, Assignment assignment) {
        Course course = getCourse(courseName);
        if (course == null) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseName);
        }
        
        course.addAssignment(assignment);
    }
    
    /**
     * Get category average for a specific course
     * 
     * @param courseName Name of the course
     * @param category Grading category
     * @return Category average percentage
     * @throws IllegalArgumentException if course not found
     */
    public double getCategoryAverage(String courseName, GradingCategory category) {
        Course course = getCourse(courseName);
        if (course == null) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseName);
        }
        
        return course.getCategoryAverage(category);
    }
    
    /**
     * Get final grade for a specific course
     * 
     * @param courseName Name of the course
     * @return CourseGrade object with percentage and letter grade
     * @throws IllegalArgumentException if course not found
     */
    public CourseGrade getCourseGrade(String courseName) {
        Course course = getCourse(courseName);
        if (course == null) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseName);
        }
        
        double percentage = course.calculateFinalGrade();
        String letterGrade = course.getFinalLetterGrade();
        return new CourseGrade(percentage, letterGrade);
    }
    
    /**
     * Calculate cumulative GPA weighted by credit hours
     * 
     * @return Cumulative GPA (0.0 - 4.0)
     */
    public double calculateGPA() {
        if (courses.isEmpty()) {
            return 0.0;
        }
        
        double totalQualityPoints = 0.0;
        int totalCreditHours = 0;
        
        for (Course course : courses) {
            double gpaPoints = course.getGpaPoints();
            int creditHours = course.getCreditHours();
            
            totalQualityPoints += gpaPoints * creditHours;
            totalCreditHours += creditHours;
        }
        
        return totalCreditHours > 0 ? totalQualityPoints / totalCreditHours : 0.0;
    }
    
    /**
     * Generate a comprehensive transcript for the student
     * 
     * @return Formatted transcript string
     */
    public String generateTranscript() {
        StringBuilder transcript = new StringBuilder();
        transcript.append("\n=== STUDENT TRANSCRIPT ===").append("\n");
        transcript.append("Student ID: ").append(studentId).append("\n");
        transcript.append("Name: ").append(name).append("\n");
        transcript.append("Cumulative GPA: ").append(String.format("%.2f", calculateGPA())).append("\n");
        transcript.append("Total Credit Hours: ").append(getTotalCreditHours()).append("\n");
        transcript.append("\nCourses Completed:\n");
        
        if (courses.isEmpty()) {
            transcript.append("  No courses enrolled\n");
        } else {
            for (Course course : courses) {
                double finalGrade = course.calculateFinalGrade();
                String letterGrade = course.getFinalLetterGrade();
                double gpaPoints = course.getGpaPoints();
                
                transcript.append(String.format("  %-20s %2d credits  %5.1f%%  %s  %.1f GPA\n",
                        course.getCourseName(),
                        course.getCreditHours(),
                        finalGrade,
                        letterGrade,
                        gpaPoints));
            }
        }
        
        transcript.append("\nAcademic Standing: ").append(getAcademicStanding()).append("\n");
        
        return transcript.toString();
    }
    
    /**
     * Get total credit hours enrolled
     * 
     * @return Total credit hours
     */
    public int getTotalCreditHours() {
        return courses.stream().mapToInt(Course::getCreditHours).sum();
    }
    
    /**
     * Determine academic standing based on GPA
     * 
     * @return Academic standing description
     */
    public String getAcademicStanding() {
        double gpa = calculateGPA();
        if (gpa >= 3.5) return "Dean's List";
        if (gpa >= 3.0) return "Good Standing";
        if (gpa >= 2.0) return "Satisfactory";
        if (gpa >= 1.0) return "Academic Warning";
        return "Academic Probation";
    }
    
    /**
     * Get detailed course information
     * 
     * @param courseName Name of the course
     * @return Detailed course report
     * @throws IllegalArgumentException if course not found
     */
    public String getCourseReport(String courseName) {
        Course course = getCourse(courseName);
        if (course == null) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseName);
        }
        
        return course.generateCourseReport();
    }
    
    // Getters
    public String getStudentId() {
        return studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }
    
    public int getCourseCount() {
        return courses.size();
    }
    
    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', courses=%d, GPA=%.2f}",
                studentId, name, courses.size(), calculateGPA());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId.equals(student.studentId);
    }
    
    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
    
    /**
     * Inner class to represent a course grade with percentage and letter grade
     */
    public static class CourseGrade {
        private final double percentage;
        private final String letterGrade;
        
        public CourseGrade(double percentage, String letterGrade) {
            this.percentage = percentage;
            this.letterGrade = letterGrade;
        }
        
        public double getPercentage() {
            return percentage;
        }
        
        public String getLetterGrade() {
            return letterGrade;
        }
        
        @Override
        public String toString() {
            return String.format("%.1f%% (%s)", percentage, letterGrade);
        }
    }
}