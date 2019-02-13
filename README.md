# Distributed Sorting Algorithms

This project is a demonstration of some common algorithms used in Distributed Sorting. 

 - **Sasaki**'s n-1 rounds algorithm. 
 - An alternative n-1 rounds algorithm which I like to call **Modulo3** algorithm.

The project makes use of multithreading and inter-thread communication to achieve the task. The code is in **Java** and **Kotlin**.

A **jar** file is available since **Kotlin plugins are required** in order to build and run the project. 

## How to run the project

A **jar** file is available, which can b run like so - 

    java -jar filename.jar

In this case the file name being 'DistributedSortingAlgorithms.jar'.

## Requirements

This available jar was built with Java JDK 1.8 (Java 8). 

## How do I build the JAR file myself ?

Since some files are in Kotlin, Kotlin plugins need to be installed along with Java JDK 1.8. So the easy way would be to **install IntelliJ Idea** (which will download everything automatically since the **.iml** file and **.idea** directory are also available)
and use it to build the artifact.