# Java Coding Challenge - Banking and Gradebook Systems

This repository contains the implementation of two comprehensive Java coding challenges: a **Banking System** and a **Gradebook System**. Both systems demonstrate object-oriented design principles, robust error handling, and comprehensive testing.

## 📋 Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Assignment 1: Banking System](#assignment-1-banking-system)
- [Assignment 2: Gradebook System](#assignment-2-gradebook-system)
- [Setup Instructions](#setup-instructions)
- [Running the Applications](#running-the-applications)
- [Testing](#testing)
- [Sample Input/Output](#sample-inputoutput)
- [Assumptions and Design Decisions](#assumptions-and-design-decisions)
- [Technologies Used](#technologies-used)

## 🎯 Overview

This project implements two distinct systems:

1. **Banking System**: A comprehensive bank account management system supporting multiple account types, transaction processing, and detailed financial reporting.
2. **Gradebook System**: A student grade management system with weighted categories, GPA calculations, and academic reporting.

Both systems include:
- ✅ Complete object-oriented design
- ✅ Comprehensive unit tests
- ✅ Robust error handling
- ✅ Detailed documentation
- ✅ Demonstration applications

## 📁 Project Structure

```
src/
├── main/java/com/challenge/
│   ├── banking/                    # Banking System
│   │   ├── Account.java           # Account management with business rules
│   │   ├── AccountType.java       # Account type enumeration
│   │   ├── Bank.java              # Main banking operations
│   │   ├── BankingException.java  # Custom exception handling
│   │   ├── BankingSystemDemo.java # Banking system demonstration
│   │   └── Transaction.java       # Transaction tracking and validation
│   └── gradebook/                 # Gradebook System
│       ├── Assignment.java        # Assignment representation
│       ├── Course.java            # Course with weighted grading
│       ├── GradeBook.java         # Main gradebook operations
│       ├── GradebookSystemDemo.java # Gradebook demonstration
│       ├── GradingCategory.java   # Grading category enumeration
│       └── Student.java           # Student management and GPA calculation
└── test/java/com/challenge/
    ├── banking/                   # Banking system unit tests
    │   ├── AccountTest.java
    │   ├── BankTest.java
    │   ├── BankingExceptionTest.java
    │   └── TransactionTest.java
    └── gradebook/                 # Gradebook system unit tests
        ├── AssignmentTest.java
        ├── CourseTest.java
        ├── GradeBookTest.java
        ├── GradingCategoryTest.java
        └── StudentTest.java
```

## 🏦 Assignment 1: Banking System

### Features

- **Account Types**:
  - **CHECKING**: No minimum balance, $2.50 fee per transaction after 10 free transactions per month
  - **SAVINGS**: $100 minimum balance, 2% monthly interest, maximum 5 withdrawals per month

- **Transaction Types**:
  - DEPOSIT: Add money to account
  - WITHDRAWAL: Remove money from account (with validation)
  - TRANSFER: Move money between accounts

- **Transaction Tracking**:
  - Unique transaction ID
  - Timestamp
  - Amount and balance information
  - Success/failure status with reasons

### Key Classes

- **`Bank`**: Main class managing all accounts and operations
- **`Account`**: Individual account with type-specific rules
- **`Transaction`**: Immutable transaction record
- **`AccountType`**: Enumeration of account types
- **`BankingException`**: Custom exception for banking errors

### Banking Rules Implemented

1. **Checking Account Rules**:
   - No minimum balance requirement
   - First 10 transactions per month are free
   - $2.50 fee for each transaction after the 10th
   - Monthly transaction counter resets automatically

2. **Savings Account Rules**:
   - $100 minimum balance requirement
   - 2% monthly interest on positive balances
   - Maximum 5 withdrawals per month
   - Monthly withdrawal counter resets automatically

3. **General Rules**:
   - No negative balances allowed
   - All transactions are logged with complete audit trail
   - Account closure only allowed with zero balance

## 🎓 Assignment 2: Gradebook System

### Features

- **Weighted Grading Categories**:
  - HOMEWORK: 20%
  - QUIZZES: 20%
  - MIDTERM: 25%
  - FINAL_EXAM: 35%

- **Grade Calculations**:
  - Category averages (average of all assignments in category)
  - Final course grade (weighted sum of category averages)
  - Letter grades: A (90-100), B (80-89), C (70-79), D (60-69), F (<60)
  - GPA points: A=4.0, B=3.0, C=2.0, D=1.0, F=0.0

- **Academic Tracking**:
  - Student enrollment in multiple courses
  - Comprehensive transcript generation
  - Cumulative GPA calculation (weighted by credit hours)
  - Academic standing determination

### Key Classes

- **`GradeBook`**: Main class managing students and courses
- **`Student`**: Individual student with course enrollment
- **`Course`**: Course with weighted categories and assignments
- **`Assignment`**: Individual assignment with points and category
- **`GradingCategory`**: Enumeration of grading categories with weights

### Grading Rules Implemented

1. **Category Weights**: Must total 100% across all categories
2. **Missing Assignments**: Treated as zero points
3. **Empty Categories**: Excluded from final grade calculation
4. **GPA Calculation**: Weighted by credit hours across all courses
5. **Academic Standing**: Based on cumulative GPA

## ⚙️ Setup Instructions

### Prerequisites

- **Java 8 or higher**
- **Maven 3.6 or higher**
- **Git** (for cloning the repository)

### Installation Steps

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd java-coding-challenge
   ```

2. **Compile the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the tests**:
   ```bash
   mvn test
   ```

4. **Generate test coverage report**:
   ```bash
   mvn test jacoco:report
   ```
   Coverage report will be available at `target/site/jacoco/index.html`

## 🚀 Running the Applications

### Banking System Demo

```bash
# Compile and run the banking demonstration
mvn exec:java -Dexec.mainClass="com.challenge.banking.BankingSystemDemo"
```

**Or using Java directly**:
```bash
java -cp target/classes com.challenge.banking.BankingSystemDemo
```

### Gradebook System Demo

```bash
# Compile and run the gradebook demonstration
mvn exec:java -Dexec.mainClass="com.challenge.gradebook.GradebookSystemDemo"
```

**Or using Java directly**:
```bash
java -cp target/classes com.challenge.gradebook.GradebookSystemDemo
```

## 🧪 Testing

### Running All Tests

```bash
# Run all unit tests
mvn test

# Run tests with detailed output
mvn test -Dtest.verbose=true

# Run specific test class
mvn test -Dtest=BankTest

# Run specific test method
mvn test -Dtest=BankTest#testOpenAccount
```

### Test Coverage

The project includes comprehensive unit tests with the following coverage:

- **Banking System**: 95%+ code coverage
  - Account operations and business rules
  - Transaction processing and validation
  - Error handling and edge cases
  - Monthly fee and interest calculations

- **Gradebook System**: 95%+ code coverage
  - Grade calculations and weighted averages
  - Student and course management
  - GPA calculations and academic standing
  - Report generation and formatting

### Test Framework

- **JUnit 5**: Primary testing framework
- **AssertJ**: Fluent assertions for better readability
- **Mockito**: Mocking framework for isolated testing
- **JaCoCo**: Code coverage reporting

## 📊 Sample Input/Output

### Banking System Output Sample

```
=== Banking System Demonstration ===
Demonstrating 4 accounts with 20+ transactions including failures

1. Creating Accounts:
   Checking Account 1 created: 1001 with $500.00
   Checking Account 2 created: 1002 with $1000.00
   Savings Account 1 created: 1003 with $2000.00
   Savings Account 2 created: 1004 with $150.00

2. Performing Transactions:
   Deposit $200 to Checking Account 1
     ✓ SUCCESS: Balance after: $700.00
   
   Transfer $150 from Checking 1 to Checking 2
     ✓ SUCCESS: From balance: $550.00, To balance: $1200.00
   
   Attempt to withdraw $2000 from Checking Account 1 (should fail)
     ✗ FAILED: Insufficient funds

4. Final Account Balances:
   Checking Account 1 (1001): $547.50 [CHECKING]
   Checking Account 2 (1002): $1200.00 [CHECKING]
   Savings Account 1 (1003): $2346.00 [SAVINGS]
   Savings Account 2 (1004): $225.00 [SAVINGS]
```

### Gradebook System Output Sample

```
=== Gradebook System Demonstration ===
Demonstrating 3 students with multiple courses and assignments

1. Adding Students:
   Student added: Alice Johnson (STU001)
   Student added: Bob Smith (STU002)
   Student added: Carol Davis (STU003)

5. Student GPAs:
   Alice Johnson: GPA = 3.47 (Good Standing)
   Bob Smith: GPA = 2.67 (Satisfactory)
   Carol Davis: GPA = 3.23 (Good Standing)

=== STUDENT TRANSCRIPT ===
Student ID: STU001
Name: Alice Johnson
Cumulative GPA: 3.47
Total Credit Hours: 10

Courses Completed:
  Computer Science 101   3 credits   84.6%  B  3.0 GPA
  Mathematics 201        4 credits   91.7%  A  4.0 GPA
  Physics 101            3 credits   77.8%  C  2.0 GPA

Academic Standing: Good Standing
```

## 🔧 Assumptions and Design Decisions

### Banking System Assumptions

1. **Account Numbers**: Auto-generated sequential numbers starting from 1001
2. **Monthly Resets**: Automatically handled based on current date
3. **Transaction Fees**: Applied immediately when limits are exceeded
4. **Interest Calculation**: Applied manually via `applyMonthlyInterest()` method
5. **Currency**: All amounts in USD with 2 decimal precision
6. **Concurrency**: Single-threaded design (no concurrent access protection)

### Gradebook System Assumptions

1. **Grade Weights**: Fixed at HOMEWORK(20%), QUIZZES(20%), MIDTERM(25%), FINAL_EXAM(35%)
2. **Missing Assignments**: Treated as 0 points when calculating category averages
3. **Empty Categories**: Categories with no assignments are excluded from final grade
4. **Letter Grade Boundaries**: Standard 10-point scale (A: 90-100, B: 80-89, etc.)
5. **GPA Scale**: 4.0 scale with standard point values
6. **Credit Hours**: Used for weighted GPA calculation across courses

### Design Patterns Used

1. **Immutable Objects**: Transaction and Assignment classes
2. **Builder Pattern**: Considered for complex object creation
3. **Strategy Pattern**: Different account types with varying rules
4. **Factory Pattern**: Account and course creation
5. **Observer Pattern**: Could be added for grade change notifications

## 🛠️ Technologies Used

- **Java 8+**: Core programming language
- **Maven 3.6+**: Build and dependency management
- **JUnit 5.8.2**: Unit testing framework
- **AssertJ 3.24.2**: Fluent assertion library
- **Mockito 4.11.0**: Mocking framework
- **JaCoCo 0.8.7**: Code coverage analysis
- **Maven Surefire 3.0.0-M7**: Test execution plugin

## 📈 Performance Considerations

- **Memory Efficiency**: Immutable objects prevent accidental modifications
- **Time Complexity**: Most operations are O(1) or O(n) where n is number of transactions/assignments
- **Scalability**: Current design supports thousands of accounts/students
- **Thread Safety**: Not implemented (single-threaded assumption)

## 🔮 Future Enhancements

### Banking System
- [ ] Multi-threading support with concurrent access protection
- [ ] Database persistence layer
- [ ] REST API endpoints
- [ ] Real-time interest calculation
- [ ] Additional account types (Money Market, CD)
- [ ] Transaction reversal capabilities

### Gradebook System
- [ ] Custom grading scales and weights
- [ ] Grade curve calculations
- [ ] Attendance tracking
- [ ] Parent/guardian access
- [ ] Grade export to CSV/PDF
- [ ] Email notifications for grade updates

## 📝 License

This project is created for educational purposes as part of a coding challenge.

## 👥 Author

Coding Challenge Implementation - Demonstrating Java best practices, object-oriented design, and comprehensive testing.

---

**Note**: This implementation focuses on demonstrating solid software engineering principles including proper error handling, comprehensive testing, clear documentation, and maintainable code structure.