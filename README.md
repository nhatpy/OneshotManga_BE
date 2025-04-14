# Oneshot Manga - Backend Service 
Backend service for the FoodSpot Management System, a comprehensive platform for restaurant discovery and management in Vietnam.

## 📚 Related Repositories

- **Frontend Application**: [Oneshot Manga - Frontend](https://github.com/nhatpy/anime-social-fe)

## 🌟Core Features
- Authentication and Authorization via Jwt and Spring Security
- User, Category, Manga, Chapter management
- Review and rating service
- Real-time Notifications with Websocket
- Payment Integration with Momo UAT
- Mail Service by using SMTP

## 🛠️ Technology Stack

### Core Framework
- **Spring Boot**
- **Spring Data JPA, Hibernate**
- **Spring Security and JWT** for authentication
- **MySQL** for storing data
- **Redis** for caching

### External Services
- **Cloudinary**: Media storage
- **Java Mail Sender**: Email service
- **Momo UAT**: Payment service

### Development Tools
- **Docker & Docker Compose**
- **Swagger** for API documentation
- **Git** for version control

## 🚀 Getting Started

### Prerequisites
- JDK 21+
- MySQL and Redis 
- Docker (optional)
- TextEditor or IDE (Visual Studio 2022 or Intellij recommended)

### Local Development Setup

1. Clone the repository
```bash
git clone https://github.com/nhatpy/anime-social-be.git
cd anime-social-be
```

2. Configure your environment
```bash
follow .env.example and application.yml to config app environment
```

3. Install dependencies
```bash
mvn clean install
```

5. Run the application
```bash
mvn spring-boot:run
```

### Docker Setup

1. Build and run using Docker Compose
```bash
docker-compose up --build
#Or else run by the "Start" button from IDE you are using
```

## 🏗️ Project Structure

```

anime-social-backend/
  ├── configurations/
  ├── dto/            
  │   ├── request/     
  │   ├── response/            
  ├── controllers/        # API endpoints
  ├── services/           
  │   ├── interfaces/     # Business logic interfaces
  │   ├── implements/     # Business logic implementations  
  ├── models/             
  ├── repositories/        
  ├── exception/          # Global Exception Handler           
  ├── enums   
resources/
  ├── static/
  ├── templates/
  ├── application.yml
```

## 📚 API Documentation

- Swagger UI is available at `/swagger` in development

## 🚢 Deployment

### Production Setup
1. Update appsettings.json with production values
2. Set environment variables for sensitive data
3. Build the Docker image
```bash
docker build -t foodie-connect .
```

### Environment Variables
Required environment variables:
```
#View the .env.example
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
```bash
git checkout -b feature/YourFeature
```
3. Commit your changes
```bash
git commit -m 'Add some feature'
```
4. Push to the branch
```bash
git push origin feature/YourFeature
```
5. Open a Pull Request

### Coding Guidelines
- Follow Java coding conventions
- Use meaningful names for methods and variables
- Update documentation as needed

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
