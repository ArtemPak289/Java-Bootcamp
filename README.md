# Java Bootcamp Projects

Welcome to my Java Bootcamp repository! This monorepo contains a collection of Java projects developed to master Core Java, Object-Oriented Programming (OOP), Data Structures, Algorithms, and the Spring Boot framework.

## Repository Structure

The projects are organized into a modular Gradle structure:

1. **[Day01-Basic-Utilities](./Day01-Basic-Utilities)**: A set of basic utilities focusing on Java fundamentals, file I/O, error handling, and simple algorithms (like Fibonacci calculation).
2. **[Day02-OOP-Collections](./Day02-OOP-Collections)**: An exploration of Object-Oriented paradigms and Java Collections through a Pet Info module.
3. **[Day03-Tic-Tac-Toe-Web](./Day03-Tic-Tac-Toe-Web)**: A modern Tic-Tac-Toe Web App built with Spring Boot, serving a RESTful API and featuring an AI opponent.
4. **[Project01-Rogue-Console](./Project01-Rogue-Console)**: A console-based Roguelike game built using the JCurses library. It showcases multi-layer architecture (Domain, Presentation, Data).

## Prerequisites
- Java 21+
- Gradle (Included via Gradle Wrapper)

## How to Build

From the root directory, simply run:
```bash
./gradlew build
```

This will compile and test all subprojects simultaneously.

## Testing

To run all unit and integration tests across the monorepo:
```bash
./gradlew test
```