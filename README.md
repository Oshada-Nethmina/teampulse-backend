# TeamPulse Backend

A **Spring Boot REST API** powering the **Weekly Report Generator & Team Dashboard** application. It provides secure authentication, report management, project management, analytics, and AI assistant integration.

## Features

* REST API
* JWT Authentication
* Role-Based Authorization
* Weekly Report Management
* Project Management
* Dashboard Analytics
* Request Validation
* PostgreSQL Integration
* AI Assistant Integration (OpenAI Ready)

## Tech Stack

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA (Hibernate)
* PostgreSQL
* JWT
* Maven

## Prerequisites

* Java 17
* Maven
* PostgreSQL

## Installation

Clone the repository:

```bash
git clone https://github.com/Oshada-Nethmina/teampulse-backend.git
cd teampulse-backend
```

Install dependencies:

```bash
mvn clean install
```

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE teampulse_db;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/teampulse_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

jwt.secret=your_jwt_secret
jwt.expiration=86400000
```

### Optional AI Configuration

To enable the AI Assistant, configure your OpenAI API key:

```properties
anthropic.api-key=YOUR_ANTHROPIC_API_KEY
```

If this property is not configured, the AI chat endpoint will be unavailable, while the rest of the application will continue to function normally.

## Running the Application

Start the server:

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

The backend will be available at:

```
http://localhost:8080
```


## Project Structure

```
src/main/java/
 ├── config/
 ├── controller/
 ├── dto/
 ├── entity/
 ├── exception/
 ├── repository/
 ├── security/
 ├── service/
 └── util/
```

## Running the Database

1. Install PostgreSQL.
2. Create the database:

```sql
CREATE DATABASE teampulse_db;
```

3. Update the database credentials in `application.properties`.
4. Start PostgreSQL.
5. Run the Spring Boot application.

Hibernate will automatically create/update the database schema based on your configuration.

## AI Assistant

The backend includes an AI integration layer.

Current implementation:

* AI chat endpoint
* Prompt construction
* Report data retrieval
* Conversation handling

A valid OpenAI API key is required to generate AI responses.

## Build

```bash
mvn clean package
```

