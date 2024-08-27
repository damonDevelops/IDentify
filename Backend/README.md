# IdentifyAPI

A restful API for identify document recognition

## Project structure
This API is written with Spring and uses JWT authentication.
Docker runs a PostgreSQL server as well as a simple SQL webui that you can access from http://localhost:9000

## Prerequisites
1. Java 17 JDK https://jdk.java.net/archive/
2. Docker installed on your system
3. A good IDE (IntelliJ?) https://www.jetbrains.com/community/education/#students

## How to run
1. Clone the repo
2. Run the command "docker compose up" in the root of the project folder.
4. Install dependencies from Maven
3. After docker starts, you can start the springboot application.