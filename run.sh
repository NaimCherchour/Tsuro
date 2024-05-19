#!/bin/bash

echo "Building the application..."
mkdir -p build
javac -d build src/main/java/*.java src/main/java/*/*.java
jar cfe Tsuro.jar main.java.TsuroGame -C build .
echo "Build complete. Running the application..."
java -jar Tsuro.jar
