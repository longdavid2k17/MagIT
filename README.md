# MagIT
MagIT is complex project, writed for my bachelor's degree diploma exam need. 
It implements simple application for team worklflow managment during software development support.
The scope of the done work includes a creation of server application, based on Java programming language with 
functionality of Spring Boot framework. The frontend application, that includes user interface, was 
written in Angular framework, what is based on TypeScript programming language. Those two 
applications are named “fullstack application”, which was fully created with logic of MVC (Model 
View Controller) design pattern. Data is stored in MySQL database. Builded application supports 
software development process, and organizes historical data about past processes.

# Usage
Whole application is based on seperation whole processes for organisations. 
User can create his own organisation and invite other users to work on projects only for those organisation that belongs to.
To work with app after creation of organisation, owner can create projects, tasks and supervise whole work progress of other users.
Owner can upload attachments and other support-providing stuff for task.

# Tech-Stack
- Backend: Java 11, Spring Boot, Hibernate, Spring Security, JWT, Maven
- Frontend: Angular, Angular Material, TypeScript, Bootstrap
- Data storage: MySQL database

# How to run
Compile or deploy backend project and run it from jar file. Server app runs on 8080 port.
Frontend application needs to run npm install command in your terminal and npm start to run whole application.

Note:
There is posibility to build those two application into one jar file - all you need is usage of some maven plugins for building app like that.

# Language 
Whole application does not provide support for english - whole UI is in polish so for non-polish speakers I recommend to use translations whole site.

# Important - copyrights
As I mentioned in introduction, project was created for my university diploma exam - 
I am the only author of those code, but from the moment of examination, this code can not be used for commercial usage. 
Think about that during copying or running whole app!!!
