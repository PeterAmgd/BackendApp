# BackendApp

BackendApp is a Spring Boot-based RESTful API application designed to manage users and their contacts. It provides authentication using JWT (JSON Web Tokens), secure endpoints, and a MySQL database for persistence. The application includes features like user registration, login, and CRUD operations for contacts, with support for pagination and sorting.

## Features
- **User Management**: Register and authenticate users via email and password.
- **Contact Management**: Add, retrieve, update, and delete contacts for authenticated users.
- **JWT Authentication**: Secure endpoints with token-based authentication.
- **Pagination and Sorting**: Retrieve contacts with customizable page size and sorting options.
- **Swagger UI**: Interactive API documentation available at `/swagger-ui/index.html`.

## Prerequisites
Before running the application, ensure you have the following installed:
- **Java 21**: The application is built with Java 21.
- **Maven**: For dependency management and building the project.
- **MySQL**: A running MySQL server (e.g., version 8.0+).
- **Git**: To clone the repository (optional).

## Installation

### 1. Clone the Repository
Clone the project to your local machine:
```bash
git clone https://github.com/PeterAmgd/BackendApp.git
cd BackendApp
```
### 2. if you have docker (optional)
You can run the application using Docker. Make sure you have Docker installed and running on your machine.

```bash
docker compose up --build
```
and implement the following in the application.properties file:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=ed7f23c5df99a8d72ef8cc06a4b72555a1c8937b7984f0e758b3f77c2e9b9e06d3bb5f7545e4a35eaaf1cf9fdc1c4a31
jwt.expiration=86400000
```
### 3. if you don't have docker (optional)
You can run the application without Docker. Make sure you have Java and Maven installed on your machine.

### 4. Database Configuration
Create a MySQL database named `test` (or any name you prefer) and update the `application.properties` file with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=ed7f23c5df99a8d72ef8cc06a4b72555a1c8937b7984f0e758b3f77c2e9b9e06d3bb5f7545e4a35eaaf1cf9fdc1c4a31
jwt.expiration=86400000
```
### 5. Run the Application
Run the application using Maven:
```bash
mvn clean install
```
```bash
mvn spring-boot:run
```
### 6. Access the API Documentation
Once the application is running, you can access the Swagger UI for API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```
### 7. Test the API via Postman
you can download the postman collection from the following steps:
 direct to that link:
http://localhost:8080/v3/api-docs
and download the postman collection and import it into your postman.

## API Endpoints
### Endpoints
- **POST** `/api/auth/register`: Register a new user.
- **POST** `/api/auth/login`: Authenticate a user and receive a JWT token.
- **GET** `/api/users/user_name`: Get the specific user details by username.
- **GET** `/api/users`: Get all users.
- **GET** `api/Contacts/user_id/contact_id`: Get the specific contact details by user id and contact id.
- **GET** `/api/Contacts/user_id`: Get all contacts for a specific user.
- **POST** `/api/Contacts/user_id`: Add a new contact for a specific user.
- **GET** `/api/Contacts/sorting /user_id?page=0&size=10&sortBy=lastName`: Get all contacts for a specific user with pagination and sorting.
- **GET** `/api/Contacts/contact/contact_id`: Get the specific contact details by contact id.
