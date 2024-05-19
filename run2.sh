#!/bin/bash

echo "Building the application..."
mkdir -p build

# Compile the Java source files
javac -d build src/main/java/*.java src/main/java/*/*.java

echo "Build complete. Running the application..."

# Run the main class directly
java -cp build main.java.TsuroGame
