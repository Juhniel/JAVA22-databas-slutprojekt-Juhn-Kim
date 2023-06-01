# JAVA22-databas-slutprojekt-Juhn-Kim

This project represents a console-based banking application. 
The application provides a simple and intuitive interface for users to register accounts, log in, and perform various banking operations, all through the console.
The application is implemented in Java and follows a layered architecture:

View Layer: This is the presentation layer of the application, which interacts directly with the user. It consists of various menus (e.g., MainMenu, LoginMenu) that guide users through the possible actions they can take.

Service Layer (Business Logic): The business logic of the application is encapsulated within service classes (e.g., UserService, AccountService). These services are responsible for executing the core functionality of the application such as creating new user accounts, validating and hashing passwords, creating bank accounts, etc.

Repository Layer: The repository layer is responsible for data persistence and retrieval.

The application uses the pattern dependency injection & also the connection is initialized in a singleton class.
