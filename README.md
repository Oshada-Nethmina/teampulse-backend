# TeamPulse Backend

Backend API for the **TeamPulse – Weekly Report Generator & Team Dashboard** technical assignment.

This application provides a secure REST API that enables team members to submit weekly reports and allows managers to monitor team progress through dashboards and analytics.

---

## Features

### Authentication & Authorization
# TeamPulse Frontend

A modern **Next.js** application for the **Weekly Report Generator & Team Dashboard**. It enables team members to submit weekly reports and managers to monitor team performance through an interactive dashboard.

## Features

* JWT Authentication
* Role-Based Access Control
* Weekly Report Management
* Manager Dashboard
* Project Management
* Charts & Analytics
* AI Chat Assistant UI (API key required)
* Responsive Design
* Built with reusable React components

## Tech Stack

* Next.js
* React
* TypeScript
* Tailwind CSS
* Axios
* React Hook Form
* Recharts
* React Hot Toast

## Prerequisites

* Node.js 18+
* npm or yarn
* Backend API running

## Installation

Clone the repository:

```bash
git clone <frontend-repository-url>
cd teampulse-frontend
```

Install dependencies:

```bash
npm install
```

## Environment Variables

Create a `.env.local` file.

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## Running the Application

Development mode:

```bash
npm run dev
```

Production build:

```bash
npm run build
npm start
```

The application will be available at:

```
http://localhost:3000
```

## Project Structure

```
src/
 ├── app/
 ├── components/
 ├── hooks/
 ├── lib/
 ├── services/
 ├── types/
 └── utils/
```

## AI Assistant

The frontend includes an AI Chat interface.

To enable AI responses:

* Configure the backend with a valid OpenAI API key.
* Ensure the backend AI endpoint is available.

Without an API key, the chat interface will be visible, but AI responses will not be generated.

## Available Scripts

```bash
npm run dev
npm run build
npm run lint
```

## Backend Repository

Run the backend before starting the frontend.

## License

This project was developed as part of a technical assessment.

* JWT Authentication
* User Registration & Login
* BCrypt Password Encryption
* Role-Based Access Control (Team Member / Manager)

### Weekly Reports

* Create weekly reports
* Edit reports
* Submit reports
* View report history
* Standardized report structure

### Team Dashboard

* View reports from all team members
* Filter by team member
* Filter by project/category
* Filter by week or date range
* Track report submission status

### Project Management

* Create projects/categories
* Update projects
* Delete projects
* Assign users to projects *(optional)*

### Dashboard Analytics

* Weekly report submission metrics
* Submission compliance tracking
* Open blockers count
* Team workload insights
* Report trends and activity summaries

---

## Technology Stack

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA (Hibernate)
* PostgreSQL 
* Maven
* Lombok

---

## Project Structure

```text
src
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── util
```

---

## Getting Started

### Prerequisites

* Java 17+
* Maven 3.9+
* PostgreSQL 15+ (or later)

### Clone the Repository

```bash
git clone https://github.com/Oshada-Nethmina/teampulse-backend.git
cd teampulse-backend
```

### Configure Database

Update the database configuration in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/teampulse_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Also configure:

```properties
app.jwt.secret=your-secret-key
app.jwt.expiration-ms=86400000
app.cors.allowed-origins=http://localhost:3000
```

### Run the Application

```bash
mvn spring-boot:run
```

The backend will be available at:

```text
http://localhost:8080
```

---

## Authentication

Protected endpoints require a JWT access token.

Include the token in every authenticated request:

```http
Authorization: Bearer <jwt-token>
```

---

## REST API

The backend exposes REST APIs for:

* Authentication
* Users
* Weekly Reports
* Projects
* Dashboard
* Analytics

---

## Project Status

This project is being developed as part of a technical assignment.

Current implementation includes:

* JWT Authentication
* Spring Security
* User Management

Additional modules including Weekly Reports, Dashboard, Project Management, Analytics, and AI Assistant will be implemented incrementally.

