# Project 01 — Java_Bootcamp

**Summary:**
In this project, you will get acquainted with the basic features of the Java language.

💡 [Click here](https://new.oprosso.net/p/4cb31ec3f47a4596bc758ea1861fb624) to share your feedback on this project. It’s anonymous and will help our team improve the course. We recommend filling out the survey immediately after completing the project.


## Contents

  - [Chapter I](#chapter-i)
    - [Instructions](#instructions)
  - [Chapter II](#chapter-ii)
    - [General Information](#general-information)
  - [Chapter III](#chapter-iii)
    - [Project. Smart Utilities](#project-smart-utilities)
    - [Task 0. Creating the Project](#task-0-creating-the-project)
    - [Task 1. Calculating the Perimeter of a Triangle](#task-1-calculating-the-perimeter-of-a-triangle)
    - [Task 2. Calculating Hours, Minutes, and Seconds](#task-2-calculating-hours-minutes-and-seconds)
    - [Task 3. Finding a Fibonacci Number](#task-3-finding-a-fibonacci-number)
    - [Task 4. Calculating the Arithmetic Mean of Negative Numbers](#task-4-calculating-the-arithmetic-mean-of-negative-numbers)
    - [Task 5. Finding Numbers with Matching First and Last Digits](#task-5-finding-numbers-with-matching-first-and-last-digits)
    - [Task 6. Selection Sort of an Array](#task-6-selection-sort-of-an-array)
    - [Task 7. Finding the Maximum and Minimum Values](#task-7-finding-the-maximum-and-minimum-values)
    - [Task 8. Ascending Order Sequence](#task-8-ascending-order-sequence)
    - [Task 9. String Filtering](#task-9-string-filtering)
    - [Task 10. Finding Names of Adult Users](#task-10-finding-names-of-adult-users)

## Chapter I

### Instructions

How to learn at “School 21”:

- Here, you’ll find a unique learning experience with a lot of freedom. You’re given a task and left to find your own way to solve it, using whatever resources work best for you — whether that’s the Internet or AI tools like GigaChat. Just be mindful of information quality: verify, think critically, analyze, and compare.
- Peer-to-peer (P2P) learning is the exchange of knowledge and experience with peers, where everyone acts as both mentor and student. This approach allows you to gain a deeper understanding of the material by learning from one another.
- Feel free to ask for help: around you are peers who are also navigating this path for the first time. Share your own experience and ideas with others.  Join Rocket.Chat to stay updated with the latest community announcements. 
- Your learning is meaningless if you just copy someone else’s solutions. When receiving help from others, always make sure you fully understand the “why”, “how”, and “purpose” behind the solution. Don’t be afraid to make mistakes. 
- Does the task seem impossible? Take a break, get some fresh air and clear your mind — this has helped many people. Maybe after that, the solution will come to you naturally.
- The learning process is just as important as the result. It’s not just about completing the task — it’s about understanding HOW to solve it. 

How to work with the project:

- Before starting, clone the project from GitLab into a repository with the same name.
- All files should be created inside the _src/_ folder of the cloned repository.
- After cloning the project, create a _develop_ branch and do all your development there. Then, push the _develop_ branch to GitLab.
- Your directory should not contain any files other than those specified in the assignments.

## Chapter II

### General Information

**Java** is a fast, secure, and reliable programming language for everything from mobile applications and enterprise software to big data applications and server technologies.

The history of Java began in 1991 when Patrick Naughton, Bill Joy, and James Gosling began working on their own project. The main goal of the project was to create a next-generation hardware-software platform that would require no special knowledge to use. The idea was to create a "computer for people" that was highly reliable yet easy to use. The project was called "Green". During the work, the need arose for a new programming language that was as universal as possible and could be used on any platform. Thus "Oak" was born.

The Java programming language as we know it today did not appear immediately. For several years, developers tried to implement the new technology in various fields, from interactive television to game consoles, but failed everywhere. The first breakthrough came in 1994 with the creation of the WebRunner browser, which was capable of displaying interactive applets embedded directly into Web pages.

As the new browser gained popularity, "Oak" was given a new name in 1995. This was the birth of the Java programming language as we know it today. The browser was renamed "HotJava", but its existence soon came to an end. Meanwhile, Java began its triumphant march around the world: first, on May 23, 1995, it was officially introduced and integrated into the popular browser of the time, Netscape Navigator 2.0; in 1996, it began to be supported by what everyone now knows as Microsoft Internet Explorer (then in version 3.0).

In 1997, Java underwent several major updates, was split into several different platforms, and began to grow in popularity — a popularity that has not waned to this day.

**Main Advantages of Java:**

1. **Portability:** Java programs can run on any machine that supports the Java Virtual Machine (JVM).
1. **Object-Oriented:** Java is a fully object-oriented language that allows the creation of modular and scalable programs. The object-oriented approach encourages code reuse and simplifies maintenance.
1. **Security:** Java provides security mechanisms, such as access control and type checking, that enable the creation of more reliable applications. The Java Virtual Machine (JVM) also provides isolation of programs from the operating system.
1. **Multithreading:** Java supports multithreading, enabling the development of efficient and responsive applications capable of performing multiple tasks simultaneously.
1. **Extensive Standard Library:** Java comes with a comprehensive standard library of classes and methods to solve a wide range of problems, simplifying development.
1. **Support for Numerous Libraries and Frameworks:** There are many libraries and frameworks built on Java, such as Spring, Hibernate, Apache Struts, etc., which facilitate the creation of complex applications.
1. **Wide Adoption and Community:** Java is one of the most popular programming languages and has a huge developer community. This means that there are plenty of resources, documentation, and expertise available to support developers.
1. **Ease of Use:** Java has a relatively simple syntax, which makes it easy to develop and understand code, especially for beginners.

**Topics to Explore:**

- Entry point of the program, program structure;
- Compilation/interpretation of the program;
- Control structures of the language (sequential execution, branching, looping);
- Primitive data types (assignment, size, memory representation, operations);
- Composite data types (assignment, size, memory representation, operations);
- Input/output organization (stdin, stdout);
- Automatic, static, and dynamic memory, garbage collection;
- Complex data structures (templates and generics).

## Chapter III

### Project: Smart Utilities

This project covers the basic features of the language through a set of tools for solving various types of tasks.

**Attention!**
Each task should be organized as a separate project. For example: `T01/src/exercise0`, `T01/src/exercise1`, ..., `T01/src/exerciseN-1`, where *N* is the number of tasks. If the previous task is necessary for the next one, simply copy the previous project into the next directory and continue development there.

### Task 0. Creating the Project

Create a new Gradle project with the following parameters:

- Language: Java
- JDK: 21 LTS (use any installation method available to you)
- Gradle DSL: Kotlin

Choose any development environment you prefer:

- IntelliJ IDEA (the free edition includes everything required for Java)
- Other national equivalents of IntelliJ IDEA
- VS Code + Extension Pack for Java


### Task 1. Calculating the Perimeter of a Triangle

Develop a mathematical module that calculates the perimeter of a triangle.

- The program reads the coordinates of the vertices of the triangle.
- The program uses floating point numbers.
- The program calculates and outputs the perimeter of the triangle if the entered vertices form a triangle.
- If the entered vertices do not form a triangle, the program outputs "It's not a triangle".
- The program does not crash on wrong input; it prints: "Could not parse a number. Please try again" and retry the input.
- Accuracy: 3 decimal places.
- Use primitive types only.

|**Input**|**Output**|
| ------ | ------ |
| 1.0 <br/>  2.0 <br/> 2.0 <br/> 1.0 <br/> 5.0 <br/> 5.0 <br/> | Perimeter: 11.414 |
| 2.0 <br/>  1.0 <br/> 2.0 <br/> 1.0 <br/> 2.0 <br/> 1.0 <br/> | It's not a triangle |
| 2.0 <br/> 1.0 <br/> 2.0 <br/> 1.0 <br/> 3.0 <br/> 1.0 <br/> | It's not a triangle |

### Task 2. Calculating Hours, Minutes, and Seconds

Develop a math module that converts seconds to the format hh:mm:ss, i.e., calculates the number of hours, minutes, and seconds.

- The program reads in the number of seconds.
- The program works with integers.
- The program calculates and outputs the number of hours, minutes and seconds in the format hh:mm:ss.
- If a number of seconds less than 0 is entered, it should output: "Incorrect time."
- The program will not crash on wrong input, it will print: "Could not parse a number. Please try again" and retry the input.
- The program should have the following structure:
  - an input method;
  - a method for calculating hours, minutes, and seconds;
  - an output method;
  - all the above methods should be called from main.
- Use primitive types only.

|**Input**|**Output**|
| ------ | ------ |
| 3599 | 00:59:59 |
| 3601 | 01:00:01 |
| -100 | Incorrect time |

### Task 3. Finding a Fibonacci Number

Develop a math module that finds the n-th Fibonacci number.

- The program reads the ordinal number of the Fibonacci number.
- The program works with integers.
- Consider memory overflow.
- Use a recursive approach to find the Fibonacci number.
- The program does not crash on wrong input; it prints: "Could not parse a number. Please try again" and retry the input.
- Use only primitive types.

|**Input**|**Output**|
| ------ | ------ |
| 10 | 55 |
| 100000000 | Too large n |

### Task 4. Calculating the Arithmetic Mean of Negative Numbers

Develop a math module that calculates the arithmetic mean of negative numbers.

- The program reads the number of numbers.
- The program reads each new number into an array.
- The program works with integers.
- If there are negative numbers, the program prints their arithmetic mean, otherwise it prints: "There are no negative elements".
- If a negative number or zero is entered as the count, the program should print "Input error. Size <= 0".
- The program will not crash on wrong input; it will print: "Could not parse a number. Please try again" and retry the input.
- Use a for-loop with a precondition.
- Use only primitive types.

|**Input**|**Output**|
| ------ | ------ |
| 4 <br/> 1 2 3 4 | There are no negative elements |
| -1 | Input error. Size <= 0 |
| 4 <br/> 1 -2 3 -4 | -3 |

### Task 5. Finding Numbers with Matching First and Last Digits

Develop a math module that finds numbers where the first and last digits are the same.

- The program reads the number of numbers.
- The program reads each new number into an array.
- The program works with integers.
- If there are numbers whose first and last digits are the same, the program should print them, otherwise it will print: "There are no such items".
- Numbers with matching first and last digits should be stored in a separate array.
- If a negative number or zero is entered as the count, the program should print: "Input error. Size <= 0."
- The program will not crash on incorrect input; it will print: "Could not parse a number. Please try again" and retry the input.
- Use a while-loop with a precondition.
- There must be a separate method to determine if a number has matching first and last digits.
- Use primitive types only.

|**Input**|**Output**|
| ------ | ------ |
| 4 <br/> 100 200 300 400 | There are no such elements |
| -1 | Input error. Size <= 0 |
| 5 <br/> 1 202 300 200005 301213 | 1 202 301213 |

### Task 6. Selection Sort of an Array

Develop a math module that performs an ascending sort on an array.

- The program reads the number of numbers.
- The program reads each new number into an array.
- The program works with floating-point numbers.
- The program outputs the sorted array in ascending order.
- If a negative number or zero is entered as the count, the program should print: "Input error. Size <= 0."
- The program will not crash on incorrect input; it will print: "Could not parse a number. Please try again" and retry the input.
- There must be a separate method for sorting the array in ascending order.
- You cannot use built-in library implementations; you must write your own.
- Use primitive types only.

|**Input**|**Output**|
| ------ | ------ |
| 4 <br/> 100.0 50.0 60.0 10.0 | 10.0 50.0 60.0 100.0 |
| -1 | Input error. Size <= 0 |

### Task 7. Finding the Maximum and Minimum Values

Develop a math module that finds the maximum and minimum values in an array.

- The program reads the path to a file.
- The program reads the count of numbers from the file.
- The program reads each new number from the file into an array until the count is exceeded or the end of the file is reached.
- The program works with floating-point numbers.
- The program prints the number of numbers read and the numbers themselves.
- The program saves the minimum and maximum values found to a file named result.txt and prints them to the console: "Saving min and max values to file".
- If fewer numbers are read than specified, it should print: "Input error. Insufficient number of items".
- If the file does not exist, it should print: "Input error. File doesn't exist".
- If a negative number or zero is entered as the count, it should print: "Input error. Size <= 0".
- The program will not crash on incorrect input; it will skip the incorrect input and continue with the next value.

| file1.txt |
| ------ |
| 4 <br/> 100.0 50.0 60.0 10.0 |

| file2.txt |
| ------ |
| 0 <br/> 100.0 50.0 60.0 10.0 |

| file3.txt |
| ------ |
| 10 <br/> 100.0 50.0 60.0 10.0 |

| file4.txt |
| ------ |
| 5 <br/> 20.0 50.0 f 60.0 g 10.0 1.0|

|Input|Console Output:|For result.txt output:|
| ------ | ------ | ------ |
| file1.txt | 4 <br/> 100.0 50.0 60.0 10.0 <br/> Saving min and max values in file | 10.0 100.0 |
| file2.txt | Input error. Size <= 0 | |
| file3.txt | Input error. Insufficient number of elements | |
| file4.txt | 5 <br/> 20.0 50.0 60.0 10.0 1.0 <br/> Saving min and max values in file | 1.0 60.0 |
| fileIsNotExist.txt | Input error. File doesn't exist | |

### Task 8. Ascending Order Sequence

Develop a math module that determines whether a sequence is in ascending order.

- The program reads every new number.
- The program works with integers.
- You cannot use arrays.
- The program should determine if the sequence is in ascending order. If not, it should print: "The sequence is not ordered from the ordinal number of the number [number]", indicating the ordinal number of the first number that breaks the order.
- The program stops checking the entered sequence if it is incorrect, and outputs the following message: "The sequence is in ascending order" if at least one number was entered.
- If no numbers have been entered, it should print: "Input error".
- Use primitive types only.

|**Input**|**Output**|
| ------ | ------ |
| 1 2 3 5 4 | The sequence is not ordered from the ordinal number of the number 4 |
| а | Input error |
| 10 20 50 80 90 g | The sequence is ordered in ascending order |

### Task 9. String Filtering

Develop a module that filters a list of strings based on a substring.

- The program reads the number of strings.
- The program reads each new string into a list.
- After reading all the strings, it provides a substring for filtering as input.
- The program works with reference data types (List, String, Integer, etc.).
- The program should return the list of strings containing the given substring.
- You cannot use the Java Stream API.
- You should implement your own filtering method.

|Input|**Output**|
| ------ | ------ |
| 4 <br/> First car <br/> Second door <br/> Third message <br/> Fourth wood <br/> oo | Second door, Fourth wood |
| 2 <br/> First car <br/> Second door <br/> kek | |

### Task 10. Finding Names of Adult Users

Develop a module that finds the names of adult users.

- Create a User class with two fields: a string for the user's name and an integer for the age.
- The User class should be in a separate file.
- The program reads the number of users.
- Each user read is added to a list.
- If a negative or zero age is entered, the program will print: "Incorrect input. Age <= 0" and moves on to the next input.
- The program does not crash on wrong input; it prints: "Could not parse a number. Please try again" and repeats the input attempt.
- The program should print the names of adult users.
- The Java Stream API must be used.
- The program uses reference data types.

|**Input**|**Output**|
| ------ | ------ |
| 3 <br/> Name1 <br/> 16 <br/> Name2 <br/> 19 <br/> Name3 <br/> 18 | Name2, Name3 |
| 3 <br/> Name1 <br/> 16 <br/> Name2 <br/> 14 <br/> Name3 <br/> 13  | |
| 3 <br/> Name1 <br/> -2 <br/> Name2 <br/> 23 <br/> Name3 <br/> 13 <br/> Name4 <br/> 24 | Incorrect input. Age <= 0 <br/> Name2, Name4 |
