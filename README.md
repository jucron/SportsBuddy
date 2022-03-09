# SportsBuddy 
[![CircleCI](https://circleci.com/gh/jucron/SportsBuddy/tree/master.svg?style=svg)](https://circleci.com/gh/jucron/SportsBuddy/tree/master)
[![codecov](https://codecov.io/gh/jucron/SportsBuddy/branch/master/graph/badge.svg?token=JJ69RWVFUI)](https://codecov.io/gh/jucron/SportsBuddy)

### Website of the App: 
>[Heroku server (live testing)](https://joao-sports-buddy.herokuapp.com/)

### Motivation:
This app was initially created as the Final Project of *[CS50's Introduction to Computer Science](https://online-learning.harvard.edu/course/cs50-introduction-computer-science)*.

Website's functionality: Create sports events and centralize information of matches. 
Theme Motivation of this app: Difficulty of meeting people, for the practice of sport, for any kinds of reasons. If you recently moved to a new country/region or have lost contact with your friends, you can use the website as a tool for socializing and play sports.

#### Video Demo (for CS50's):  [Youtube video link](https://youtu.be/hkMPWurAIa8)

## Features
### Login
* Account creation with unique username and e-mail
* Password safely encoded and stored in database
* Credentials check and login with session 
### Password recovery
* Send a message to the registered e-mail containing a new password
### Account update
* Option to update Name and Password when logged in
### Match management
* Match creation with field validation
* Match participation in which you are not participating
* Match leaving in which you are participating
* Match delete if you are the owner
## Getting Started
### Data Initialization
* Use the file [configure-postgres.sql](https://github.com/jucron/SportsBuddy/blob/master/src/main/resources/scripts/configure-postgres.sql) in order to give your PostGresSQL database the necessary configuration. This way we can properly use the different profiles: `dev` and `prod`.
* Use the file [start_data-postgres.sql](https://github.com/jucron/SportsBuddy/blob/master/src/main/resources/scripts/start_data-postgres.sql) to initialize tables and constraints in your PostGresSQL database. So just run the application for the first time, it will automatically input data if not existent.

### Via Docker
1. After changes run `docker build -t sportsbuddy .` in a project root directory to build application image.
2. After image is fully built, run `docker run -d -p 8080:8080 sportsbuddy` to start the image
3. With a browser, access the app via http://localhost:8080/ 
4. (Optional) run `docker logs -t <container>` to see the logs 
#### Notes:
Database for testing is in-memory type, for fast and convenient usage. It will be updated in the future.
You won't be able to send the message via e-mail, unless account details are changed in application.properties file
## Technology 
  - Java
  - Spring-Boot
  - Java Persistence API (JPA)
  - Hibernate (object relational mapping)
  - Thymeleaf (Frontend java engine)
  - Maven (build, dependencies)
  - Testing
  - Spring Security (Form based login)
  - HTML
  - Bootstrap
  - Code coverage
  - CircleCI
