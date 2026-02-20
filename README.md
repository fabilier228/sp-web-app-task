# Phonebook Application

A modern full-stack application for managing contacts with AI-powered features. Built with Spring Boot backend (Java 21) and Angular frontend, integrated with Google GenAI for intelligent contact management.

## Tech Stack

- **Backend:** Spring Boot 4.0.3, Java 21, Maven
- **Frontend:** Angular 20.3.0, TypeScript, SCSS
- **Database:** PostgreSQL 15
- **Integration:** Google Generative AI API
- **Containerization:** Docker & Docker Compose

## Features

- Contact management (Create, Read, Update, Delete)
- AI-powered contact insights via Google GenAI integration
- RESTful API architecture
- Responsive Angular web interface
- PostgreSQL database for persistent storage

## Getting Started

### Prerequisites

- Docker and Docker Compose installed
- `.env` file configured with your environment variables (see [.env.example](.env.example) for required keys)

### Configuration

1. Copy `.env.example` to `.env` and update the values:
   ```bash
   cp .env.example .env
   ```

2. Configure the following environment variables in `.env`:
   - `GOOGLE_GENAI_API_KEY` - Your Google Generative AI API key
   - `DB_USER` - PostgreSQL username
   - `DB_PASSWORD` - PostgreSQL password
   - `DB_NAME` - Database name
   - `BACKEND_PORT` - Backend service port (default: 8080)
   - `FRONTEND_PORT` - Frontend service port (default: 80)

   For a complete list of available configuration options, see [.env.example](.env.example).

### Running the Application

Start all services (database, backend, frontend) with Docker Compose:

```bash
docker-compose up --build
```

Services will be available at:
- **Frontend:** http://localhost
- **Backend API:** http://localhost:8080

To stop the services:

```bash
docker-compose down
```

To view logs:

```bash
docker-compose logs -f
```

## Architecture

The application consists of three main components:

1. **PostgreSQL Database** - Data persistence layer
2. **Spring Boot Backend** - REST API and business logic with AI integration
3. **Angular Frontend** - User interface and client-side routing

Services communicate through a Docker network (`sp-network`) for secure inter-service communication.

## MVP Note

In the current MVP version, authentication and authorization implementation (Spring Security/JWT) has been intentionally omitted to focus on architecting the LLM integration layer for contact management features.

For production deployment, the following enhancements are planned:

- **User Authentication:** Add Spring Security with JWT token-based authentication
- **User Management:** Create a `Users` table with user profiles
- **Data Privacy:** Implement One-To-Many relationships between users and contacts
- **Session Management:** Handle user sessions to enforce data isolation
- **Personalized Features:** Ensure features like "Add to My Phone" only display private contacts for the authenticated user

This approach will provide secure, multi-tenant contact management with personalized AI features.

## License

This project is provided as-is for internal use.
