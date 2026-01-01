#!/bin/bash

echo "Compiling Java files..."
cd src
javac -cp "../lib/mysql-connector-java.jar" *.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "Running application..."
    echo "=========================="
    java -cp ".:../lib/mysql-connector-java.jar" Main
else
    echo "Compilation failed!"
    exit 1
fi
