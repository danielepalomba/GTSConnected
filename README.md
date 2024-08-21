GTSConnected represents my first experience with the Spring framework. The platform is designed to manage rides and passengers for a motorcycle club, including features for organizing, tracking, and managing group rides. Additionally, it offers an integrated chat to facilitate communication among club members. The system interfaces with external services such as AWS S3 for image management and uses hosted databases for data persistence.

## Main Features

- **User Management:** Users can register, authenticate, and manage their profiles.
- **Trip Management:** Admins can create, modify, and delete trips, specifying trip details and available passenger seats.
- **Passenger Management:** Users can view available trips and request to join as passengers.
- **Real-Time Chat:** Using WebSocket, members can communicate in real time to coordinate trip details.
- **Image Management:** Images related to user profiles and trips are stored in an AWS S3 bucket.
- **Docker:** The application is containerized with Docker to facilitate deployment and scalability.

## Project Structure

The project is divided into the following main modules:

- **Backend:** Implemented with Spring Boot, it handles business logic and data access.
- **Frontend:** Developed using the template engine Thymeleaf.
- **Database:** Utilizes a MySQL relational database for data persistence.
- **WebSocket:** Implementation of real-time chat.
- **AWS S3:** Management of uploaded images.
- **Docker:** Dockerfile and configurations for containerization.

  **To set your profile as ADMIN you have to go in UserService.java, find method UserRegistration, replace email with yours**. 
  **You also have to specify your bucket S3, databse and your gmail config to make this project works**.
  **You can do this easly by setting your parameters in application.properties and Docker file**

Try it: https://gtsconnected3.onrender.com
