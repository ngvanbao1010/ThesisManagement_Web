# Thesis Management System

A comprehensive web-based application for managing university thesis defense processes, built with Spring Boot backend and React frontend.

## Project Overview

This Thesis Management System streamlines the entire thesis defense workflow, from thesis creation to evaluation and committee management. The system supports multiple user roles including students, lecturers, academic staff, and administrators.

## Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.x with Jakarta EE 11
- **Security**: Spring Security 6.x with JWT authentication
- **Database**: MySQL with Hibernate ORM
- **File Storage**: Cloudinary integration for file uploads
- **Email**: Jakarta Mail for notifications
- **PDF Generation**: OpenHTMLToPDF for report generation
- **Build Tool**: Maven

### Frontend (React)
- **Framework**: React 19.x
- **UI Library**: React Bootstrap 5.x
- **Routing**: React Router DOM 7.x
- **HTTP Client**: Axios
- **Styling**: Bootstrap CSS
- **State Management**: React Context + useReducer

## Features

### User Management
- **Multi-role authentication**: Student, Lecturer, Academic Staff, Admin
- **JWT-based security** with role-based access control
- **Profile management** with avatar upload
- **Password change** functionality

### Thesis Management
- **Thesis creation** by academic staff
- **Student-supervisor assignment**
- **File upload** for thesis documents
- **Major categorization** (IT, Logistics, Accounting, etc.)
- **Status tracking** (Pending, Defended, Closed)

### Committee Management
- **Defense committee creation** with multiple members
- **Role assignment** (Chairman, Secretary, Reviewer, Member)
- **Committee locking** mechanism
- **Email notifications** to committee members

### Evaluation System
- **Configurable evaluation criteria**
- **Score submission** by committee members
- **Average score calculation**
- **Detailed evaluation reports**

### Analytics & Reporting
- **Score trends by year**
- **Thesis count by major**
- **PDF report generation**
- **Statistical dashboards**

## Technology Stack

### Backend Dependencies
```xml
- Spring Boot 3.3.5
- Spring Security 6.3.4
- Hibernate 6.6.19
- MySQL Connector 8.4.0
- Cloudinary 1.29.0
- Jackson 2.19.1
- Nimbus JOSE JWT 10.1
- OpenHTMLToPDF 1.0.10
- Jakarta Mail 2.0.1
```

### Frontend Dependencies
```xml
- React 19.1.1
- React Bootstrap 2.10.10
- React Router DOM 7.7.1
- Axios 1.11.0
- React Select 5.10.2
- Bootstrap 5.3.7
```

## Installation & Setup

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### Backend Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd ThesisApp
```

2. **Configure Database**
Create a MySQL database and update `src/main/resources/database.properties`:
```properties
hibernate.connection.url=jdbc:mysql://localhost:3306/thesisapp
hibernate.connection.username=your_username
hibernate.connection.password=your_password
```

3. **Build and Run**
```bash
mvn clean compile
mvn tomcat7:run
# Or deploy the WAR file to your servlet container
```

The backend will be available at `http://localhost:8080/ThesisApp`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd thesisweb
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure API endpoint**
Update `src/configs/Apis.js` if needed:
```javascript
const BASE_URL = "http://localhost:8080/ThesisApp/api";
```

4. **Start development server**
```bash
npm start
```

The frontend will be available at `http://localhost:3000`
