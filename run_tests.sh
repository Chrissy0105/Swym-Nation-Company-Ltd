#!/bin/bash

# Configuration
H2_JAR=lib/h2-2.4.240.jar
JDBC_URL="jdbc:h2:./swymdb"
# Path to the cleanup script
SQL_CLEANUP_FILE="test_data/cleanup_db.sql"
USER="sa"
PASS=""

# Compilation Phase
echo "Compiling all Java files..."
rm -rf out
mkdir -p out

# Compile all Java files
javac -cp "$H2_JAR" -d out $(find src/main/java -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed. Fix the errors above before running tests."
    exit 1
fi

echo "Compilation successful!"
echo

# Cleanup Phase
echo "===== Cleaning Database for Fresh Run ====="
# Execute SQL cleanup to drop and recreate tables
java -cp "$H2_JAR" org.h2.tools.RunScript -url "$JDBC_URL" -user "$USER" -password "$PASS" -script "$SQL_CLEANUP_FILE" -continueOnError

if [ $? -ne 0 ]; then
    echo "Database cleanup script execution failed. Ensure $SQL_CLEANUP_FILE exists and contains valid SQL."
    exit 1
fi

echo "Database cleanup complete."
echo

# Data Injection Phase 

echo "===== Injecting Test Data ====="
java -cp "out:$H2_JAR" com.swym.services.DataInitializer

if [ $? -ne 0 ]; then
    echo "Data Injection failed. Check DataInitializer.java and the database connection."
    exit 1
fi

echo "Data injection complete."
echo

# Test Execution Phase

# Run ProgressManagerTest 
echo "===== Running ProgressManagerTest ====="
java -cp "out:$H2_JAR" com.swym.services.ProgressManagerTest
echo

# Run ReportGeneratorTest 
echo "===== Running ReportGeneratorTest ====="
java -cp "out:$H2_JAR" com.swym.services.ReportGeneratorTest
echo

# Run RegistrationTest 
echo "===== Running RegistrationTest ====="
java -cp "out:$H2_JAR" com.swym.services.RegistrationTest
echo

echo "===== All tests completed ====="