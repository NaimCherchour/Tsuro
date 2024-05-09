#!/bin/bash

# Compile Java files
javac -cp "lib/*:build" -d build src/main/java/*.java src/main/java/*/*.java src/Test/java/*/*.java

# Run unit tests
java -cp "lib/*:build" org.junit.runner.JUnitCore \
    Test.java.model.TuileTest \
    Test.java.model.TuilesGeneratorTest \
    Test.java.model.JoueurTest \
    Test.java.model.GameTest \
    Test.java.model.PlateauTuilesTest \
    Test.java.model.BotTsuroTest \
    Test.java.model.DeckTuilesTest \
    Test.java.controller.ControllerTest

# Run a Frame Test
java -cp "lib/*:build" Test.java.vue.TestDessinateurDeTuile