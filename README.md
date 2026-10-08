# Basketball League Management System

A Java-based console application for managing a basketball league, including teams, players, games, results, and standings.

The project uses PostgreSQL for persistent data storage and JDBC for database connectivity. It was built as a learning project to strengthen my understanding of object-oriented programming, relational databases, SQL, and Java database integration.

## Features

- Add and manage teams
- Add players and assign them to teams
- Prevent duplicate teams and duplicate players within the same team
- Schedule games between teams
- Prevent a team from playing against itself
- Prevent duplicate pending games
- Enter and validate game results
- Reject negative scores and tied final scores
- Automatically update team wins and losses
- Display league standings
- Persist teams, players, games, and results using PostgreSQL
- Reload saved data when the application restarts
- Validate user input and handle invalid menu choices
- Format team and player names consistently
- Provide readable SQL views for teams, players, and games

## Technologies Used

- Java
- PostgreSQL
- JDBC
- SQL
- Object-Oriented Programming
- Eclipse IDE

## Project Structure

src/
└── project/
    ├── Main.java
    ├── Team.java
    ├── Player.java
    ├── Game.java
    ├── Database.java
    ├── TeamDAO.java
    ├── PlayerDAO.java
    └── GameDAO.java
schema.sql
README.md