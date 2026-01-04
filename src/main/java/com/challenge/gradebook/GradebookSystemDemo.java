package com.challenge.gradebook;

/**
 * Demonstration class showing the gradebook system with 3 students
 * each enrolled in 2-3 courses with varying assignments.
 */
public class GradebookSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Gradebook System Demonstration ===");
        System.out.println("Demonstrating 3 students with multiple courses and assignments\n");
        
        GradeBook gradeBook = new GradeBook();
        
        try {
            // Create 3 students
            System.out.println("1. Adding Students:");
            gradeBook.addStudent("STU001", "Alice Johnson");
            System.out.println("   Student added: Alice Johnson (STU001)");
            
            gradeBook.addStudent("STU002", "Bob Smith");
            System.out.println("   Student added: Bob Smith (STU002)");
            
            gradeBook.addStudent("STU003", "Carol Davis");
            System.out.println("   Student added: Carol Davis (STU003)");
            
            // Enroll students in courses
            System.out.println("\n2. Enrolling Students in Courses:");
            
            // Alice: 3 courses
            gradeBook.enrollInCourse("STU001", "Computer Science 101", 3);
            gradeBook.enrollInCourse("STU001", "Mathematics 201", 4);
            gradeBook.enrollInCourse("STU001", "Physics 101", 3);
            System.out.println("   Alice enrolled in 3 courses (10 credit hours)");
            
            // Bob: 2 courses
            gradeBook.enrollInCourse("STU002", "Computer Science 101", 3);
            gradeBook.enrollInCourse("STU002", "English 101", 3);
            System.out.println("   Bob enrolled in 2 courses (6 credit hours)");
            
            // Carol: 3 courses
            gradeBook.enrollInCourse("STU003", "Mathematics 201", 4);
            gradeBook.enrollInCourse("STU003", "English 101", 3);
            gradeBook.enrollInCourse("STU003", "History 101", 3);
            System.out.println("   Carol enrolled in 3 courses (10 credit hours)");
            
            // Add assignments for Alice - Computer Science 101
            System.out.println("\n3. Adding Assignments:");
            System.out.println("\n   Alice - Computer Science 101:");
            
            // Homework assignments (20% weight)
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("HW1 - Variables", 85, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("HW2 - Loops", 92, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("HW3 - Functions", 88, 100, GradingCategory.HOMEWORK));
            System.out.println("     Added 3 homework assignments");
            
            // Quiz assignments (20% weight)
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("Quiz 1", 78, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("Quiz 2", 85, 100, GradingCategory.QUIZZES));
            System.out.println("     Added 2 quiz assignments");
            
            // Midterm (25% weight)
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("Midterm Exam", 82, 100, GradingCategory.MIDTERM));
            System.out.println("     Added midterm exam");
            
            // Final exam (35% weight)
            gradeBook.addAssignment("STU001", "Computer Science 101", 
                new Assignment("Final Exam", 87, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added final exam");
            
            // Add assignments for Alice - Mathematics 201
            System.out.println("\n   Alice - Mathematics 201:");
            gradeBook.addAssignment("STU001", "Mathematics 201", 
                new Assignment("HW1 - Calculus", 95, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU001", "Mathematics 201", 
                new Assignment("HW2 - Derivatives", 90, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU001", "Mathematics 201", 
                new Assignment("Quiz 1", 88, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU001", "Mathematics 201", 
                new Assignment("Midterm", 91, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU001", "Mathematics 201", 
                new Assignment("Final", 93, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 5 assignments");
            
            // Add assignments for Alice - Physics 101
            System.out.println("\n   Alice - Physics 101:");
            gradeBook.addAssignment("STU001", "Physics 101", 
                new Assignment("Lab 1", 75, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU001", "Physics 101", 
                new Assignment("Lab 2", 80, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU001", "Physics 101", 
                new Assignment("Quiz 1", 72, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU001", "Physics 101", 
                new Assignment("Midterm", 78, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU001", "Physics 101", 
                new Assignment("Final", 81, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 5 assignments");
            
            // Add assignments for Bob - Computer Science 101
            System.out.println("\n   Bob - Computer Science 101:");
            gradeBook.addAssignment("STU002", "Computer Science 101", 
                new Assignment("HW1 - Variables", 78, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU002", "Computer Science 101", 
                new Assignment("HW2 - Loops", 85, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU002", "Computer Science 101", 
                new Assignment("Quiz 1", 70, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU002", "Computer Science 101", 
                new Assignment("Quiz 2", 75, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU002", "Computer Science 101", 
                new Assignment("Midterm Exam", 73, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU002", "Computer Science 101", 
                new Assignment("Final Exam", 79, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 6 assignments");
            
            // Add assignments for Bob - English 101
            System.out.println("\n   Bob - English 101:");
            gradeBook.addAssignment("STU002", "English 101", 
                new Assignment("Essay 1", 88, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU002", "English 101", 
                new Assignment("Essay 2", 92, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU002", "English 101", 
                new Assignment("Grammar Quiz", 85, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU002", "English 101", 
                new Assignment("Midterm", 90, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU002", "English 101", 
                new Assignment("Final", 94, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 5 assignments");
            
            // Add assignments for Carol - Mathematics 201
            System.out.println("\n   Carol - Mathematics 201:");
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("HW1", 98, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("HW2", 95, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("HW3", 97, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("Quiz 1", 92, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("Quiz 2", 94, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("Midterm", 96, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU003", "Mathematics 201", 
                new Assignment("Final", 98, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 7 assignments");
            
            // Add assignments for Carol - English 101
            System.out.println("\n   Carol - English 101:");
            gradeBook.addAssignment("STU003", "English 101", 
                new Assignment("Essay 1", 85, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "English 101", 
                new Assignment("Essay 2", 88, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "English 101", 
                new Assignment("Quiz 1", 82, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU003", "English 101", 
                new Assignment("Midterm", 87, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU003", "English 101", 
                new Assignment("Final", 89, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 5 assignments");
            
            // Add assignments for Carol - History 101
            System.out.println("\n   Carol - History 101:");
            gradeBook.addAssignment("STU003", "History 101", 
                new Assignment("Research Paper", 76, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "History 101", 
                new Assignment("Timeline Project", 82, 100, GradingCategory.HOMEWORK));
            gradeBook.addAssignment("STU003", "History 101", 
                new Assignment("Quiz 1", 78, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU003", "History 101", 
                new Assignment("Quiz 2", 80, 100, GradingCategory.QUIZZES));
            gradeBook.addAssignment("STU003", "History 101", 
                new Assignment("Midterm", 74, 100, GradingCategory.MIDTERM));
            gradeBook.addAssignment("STU003", "History 101", 
                new Assignment("Final", 77, 100, GradingCategory.FINAL_EXAM));
            System.out.println("     Added 6 assignments");
            
            // Display course grades and category averages
            System.out.println("\n4. Course Grades and Category Averages:");
            
            displayStudentGrades(gradeBook, "STU001", "Alice");
            displayStudentGrades(gradeBook, "STU002", "Bob");
            displayStudentGrades(gradeBook, "STU003", "Carol");
            
            // Display GPAs
            System.out.println("\n5. Student GPAs:");
            displayGPA(gradeBook, "STU001", "Alice Johnson");
            displayGPA(gradeBook, "STU002", "Bob Smith");
            displayGPA(gradeBook, "STU003", "Carol Davis");
            
            // Generate transcripts
            System.out.println("\n6. Student Transcripts:");
            System.out.println(gradeBook.generateTranscript("STU001"));
            System.out.println(gradeBook.generateTranscript("STU002"));
            System.out.println(gradeBook.generateTranscript("STU003"));
            
            // Generate class rosters
            System.out.println("\n7. Class Rosters:");
            System.out.println(gradeBook.generateClassRoster("Computer Science 101"));
            System.out.println(gradeBook.generateClassRoster("Mathematics 201"));
            
            // Generate summary report
            System.out.println("\n8. Gradebook Summary:");
            System.out.println(gradeBook.generateSummaryReport());
            
            // Test edge cases
            System.out.println("\n9. Testing Edge Cases:");
            
            // Test missing assignments (category with no assignments)
            gradeBook.enrollInCourse("STU001", "Test Course", 1);
            gradeBook.addAssignment("STU001", "Test Course", 
                new Assignment("Only Homework", 90, 100, GradingCategory.HOMEWORK));
            
            Student.CourseGrade testGrade = gradeBook.getCourseGrade("STU001", "Test Course");
            System.out.println("   Course with only homework: " + testGrade);
            
            // Test error handling
            try {
                gradeBook.addStudent("STU001", "Duplicate Student");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ Duplicate student error handled: " + e.getMessage());
            }
            
            try {
                gradeBook.getCourseGrade("STU999", "Nonexistent Course");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ Nonexistent student error handled: " + e.getMessage());
            }
            
            System.out.println("\n=== Demonstration Complete ===");
            System.out.println("Total students: " + gradeBook.getStudentCount());
            System.out.println("All gradebook operations demonstrated successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during demonstration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Display grades for all courses for a student
     */
    private static void displayStudentGrades(GradeBook gradeBook, String studentId, String studentName) {
        try {
            Student student = gradeBook.getStudentInfo(studentId);
            System.out.println("\n   " + studentName + " Course Grades:");
            
            for (Course course : student.getCourses()) {
                String courseName = course.getCourseName();
                Student.CourseGrade grade = gradeBook.getCourseGrade(studentId, courseName);
                
                System.out.println("     " + courseName + ": " + grade);
                
                // Show category breakdowns
                for (GradingCategory category : GradingCategory.values()) {
                    double categoryAvg = gradeBook.getCategoryAverage(studentId, courseName, category);
                    if (categoryAvg > 0) { // Only show categories with assignments
                        System.out.println("       " + category.name() + ": " + 
                                         String.format("%.1f%%", categoryAvg));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("   Error displaying grades for " + studentName + ": " + e.getMessage());
        }
    }
    
    /**
     * Display GPA for a student
     */
    private static void displayGPA(GradeBook gradeBook, String studentId, String studentName) {
        try {
            double gpa = gradeBook.calculateGPA(studentId);
            Student student = gradeBook.getStudentInfo(studentId);
            System.out.println("   " + studentName + ": GPA = " + String.format("%.2f", gpa) + 
                             " (" + student.getAcademicStanding() + ")");
        } catch (Exception e) {
            System.out.println("   Error calculating GPA for " + studentName + ": " + e.getMessage());
        }
    }
}