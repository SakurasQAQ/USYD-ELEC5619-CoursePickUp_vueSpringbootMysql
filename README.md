# USYD ELEC5619 - CoursePickUp System

This project was developed for the University of Sydney's ELEC5619 "Object Oriented Application Frameworks" course. It aims to build an efficient and user-friendly course selection system using Vue.js for the frontend, Spring Boot for the backend, and MySQL for data storage.

## Project Introduction

**Duration**: 2024.03 - 2024.06  
**Institution**: The University of Sydney, School of Electrical and Information Engineering

**Description**:
The CoursePickUp system is a web-based application designed for university students to browse, select, and manage courses, with additional administrator management functionalities. It adopts a front-end and back-end separation architecture, ensuring smooth interactions on the front-end and stable and secure API services on the back-end. JWT-based authentication and role-based access control are integrated to enhance system security. The project was successfully deployed and demonstrated within the course.

## Technology Stack

- **Frontend**: Vue.js
- **Backend**: Spring Boot
- **Database**: MySQL
- **Other**: Spring Security, JWT, Axios

## Project Structure

```
CoursePickUp_vueSpringbootMysql/
├── backend/                 # Backend code (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   └── pom.xml              # Maven configuration file
├── frontend/                # Frontend code (Vue.js)
│   ├── public/
│   ├── src/
│   └── package.json         # npm configuration file
└── README.md                # Project description file
```

## Core Features

- User registration and login (JWT-based authentication)
- Browse available courses
- Add and drop courses
- View personal timetable
- Admin functionalities: manage courses and student information

## Installation and Deployment Guide

### Backend (Spring Boot)

1. Ensure MySQL is installed and running.
2. Create a database (e.g., `course_pickup`).
3. Configure `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/course_pickup
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Navigate to the `backend` directory and run:

```bash
mvn spring-boot:run
```

### Frontend (Vue.js)

1. Navigate to the `frontend` directory and install dependencies:

```bash
npm install
```

2. Start the frontend development server:

```bash
npm run serve
```

3. Access the system via `http://localhost:8080`.

## License

This project is for educational purposes only and is not licensed for commercial use.

