#!/bin/bash

# Set H2 jar path
H2_JAR=lib/h2-2.4.240.jar

# Compiling all Java files
echo "Compiling all Java files..."
rm -rf out
mkdir -p out

javac -cp "$H2_JAR" -d out $(find src/main/java -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed. Fix the errors above before running tests."
    exit 1
fi

echo "Compilation successful!"
echo

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
