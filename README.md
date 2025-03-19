# Recipe Management System Rest API
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)  ![Postgres](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white) ![OpenApi](https://img.shields.io/badge/Docs-OpenAPI-success?style=for-the-badge&logo=swagger)
![SpringSecurity](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-323330?style=for-the-badge&logo=json-web-tokens&logoColor=pink)

I developed a Rest API to manage recipes and their ingredient, built by using **Spring Boot, Java, and Postgres as the Database**, providing CRUD (Create, Read, Update, Delete) operations with authentication control enabled through **Spring Security and JWT tokens**. This API allows storing recipe information, such as: title, instructions, description, and ingredients belonging to that recipe, which includes: amount, ingredient unit, and ingredient type. Finally, to ensure security, storing user information such as username and password is necessary for all user types, including admin and user.

I used some libraries for this Rest API such **Spring Web, Spring Data JPA, Validation, ModelMapper, PostgreSQL Driver, SpringDoc OpenAPI Starter WebMVC UI (for the API documentation), Spring Security and java-jwt**.

## Requirements

Below are some business rules that are essential for the system's functionality:

• The user email is unique in the system, therefore, only one registration with the same email is allowed. Additionally, the recipe description and serving are both optional fields.

• A user can have multiple recipes, while each recipe belongs to only one user. A recipe can have multiple ingredients, while each ingredient belongs to only one recipe.

• Allows the creation, updating, and removal of a user, recipe, or ingredient from the system.

• Allows users to retrieve the list of recipes, with optional filtering by title or username. Note that filtering supports both exact matches (full string comparison) and partial matches (e.g., searching for substrings within the title or username).

• The ingredient unit simply represents the unit of measurement, such as G (Gram), KG (Kilogram), ML (Milliliter), L (Liter), PC (Piece), TSP (Teaspoon), TBSP (Tablespoon), and PINCH (A dash). The ingredient type simply refers to the ingredient itself (e.g., egg, milk, salt, etc.).


## Authentication
The API uses Spring Security for authentication control. The following roles are available:

```
USER -> Standard user role, limited to default basic operations (e.g., managing their own data, such as user information, recipes, and ingredients).
ADMIN -> Administrative role with permissions to create new users, manage all recipes and ingredients, and perform advanced actions (e.g., deletions).
```

## Database Initialization with Default Data
For this project, default users have been created for quick testing using the data.sql file.

The default users were created with the following credentials:

- Username: pedro, Password: 123 (USER role).

- Username: marcos, Password: 123 (both USER and ADMIN roles).

## Database Config
For this API, the MySQL Database was used with the following configuration properties: 

- Database name: academic_system_with_security_db
- Username: root
- Password:

## Database Initialization Configuration
To run the application correctly, for the first time make sure the database already exists and set the following configuration in the application.properties file:

```
spring.sql.init.mode=always
```

For subsequent runs (once the database and its tables are already created), change the setting to:

```
spring.sql.init.mode=embedded
```

## Development Tools
This Rest API was built with:

- Spring Boot version: 3.3.4
- Java version: 17

## System Class Diagram

![AcademicSystemWithSecurityClassDiagram](https://github.com/user-attachments/assets/5b5d737b-4741-4d10-b528-528e17772a72)
