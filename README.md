# Candlesty - AI-Powered Trading Platform

A modern job application website for Candlesty, a company focused on revolutionizing automated trading through AI technology.

## Features

- **Modern UI/UX**: Beautiful, responsive design using Tailwind CSS
- **Job Application System**: Complete application form with file upload
- **Email Notifications**: Automatic confirmation emails to applicants
- **Admin Panel**: Manage applications, update status, download resumes
- **Database Integration**: MySQL database with JPA/Hibernate
- **File Management**: Secure resume upload and storage
- **Cloud Ready**: Docker configuration for easy deployment

## Technology Stack

- **Backend**: Spring Boot 3.5.3, Java 17
- **Database**: MySQL 8.0
- **Frontend**: Thymeleaf, Tailwind CSS, JavaScript
- **Email**: Spring Mail (Gmail SMTP)
- **File Storage**: Local file system
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 (or Docker)
- Docker & Docker Compose (for containerized deployment)

## Quick Start with Docker

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd candlesty
   ```

2. **Configure email settings**
   Edit `docker-compose.yml` and update the email configuration:
   ```yaml
   environment:
     - SPRING_MAIL_USERNAME=your-email@gmail.com
     - SPRING_MAIL_PASSWORD=your-app-password
   ```

3. **Start the application**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
   - Main site: http://localhost:8080
   - Admin panel: http://localhost:8080/admin/applications

## Local Development Setup

1. **Database Setup**
   ```bash
   # Start MySQL (if using Docker)
   docker run --name mysql-candlesty -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=candlesty_jobs -p 3306:3306 -d mysql:8.0
   ```

2. **Configure application.properties**
   Update `src/main/resources/application.properties` with your database and email settings:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=password
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

## Email Configuration

To enable email notifications, you need to configure Gmail SMTP:

1. **Enable 2-Factor Authentication** on your Gmail account
2. **Generate an App Password**:
   - Go to Google Account settings
   - Security → 2-Step Verification → App passwords
   - Generate a password for "Mail"
3. **Update the configuration** with your email and app password

## Project Structure

```
candlesty/
├── src/main/java/in/cd/main/
│   ├── controller/          # Web controllers
│   ├── entity/             # JPA entities
│   ├── repository/          # Data access layer
│   ├── service/            # Business logic
│   └── service/impl/       # Service implementations
├── src/main/resources/
│   ├── templates/          # Thymeleaf templates
│   └── application.properties
├── uploads/               # Resume storage
├── Dockerfile             # Docker configuration
├── docker-compose.yml     # Docker Compose setup
└── README.md
```

## Available Endpoints

- `GET /` - Home page
- `GET /jobs` - Job listings
- `GET /apply` - Application form
- `POST /apply` - Submit application
- `GET /admin/applications` - Admin panel
- `POST /admin/application/{id}/status` - Update application status
- `DELETE /admin/application/{id}` - Delete application
- `GET /admin/resume/{filename}` - Download resume

## Admin Features

- View all applications in a table format
- Update application status (Pending, Reviewed, Accepted, Rejected)
- Download applicant resumes
- Delete applications
- Track email confirmation status

## Deployment

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up -d

# For production, use environment variables
docker-compose -f docker-compose.prod.yml up -d
```

### Cloud Deployment

The application is ready for deployment on various cloud platforms:

- **AWS**: Use AWS RDS for MySQL, S3 for file storage
- **Google Cloud**: Use Cloud SQL and Cloud Storage
- **Azure**: Use Azure Database for MySQL and Blob Storage
- **Heroku**: Use Heroku Postgres and AWS S3
- **DigitalOcean**: Use Managed MySQL and Spaces

### Environment Variables

For production deployment, set these environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/candlesty_jobs
SPRING_DATASOURCE_USERNAME=your-db-user
SPRING_DATASOURCE_PASSWORD=your-db-password
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password
```

## Security Considerations

- File upload validation (PDF, DOC, DOCX only)
- File size limits (10MB max)
- Secure file storage with unique filenames
- Email validation and sanitization
- CSRF protection enabled

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support or questions, please contact:
- Email: info@candlesty.com
- Website: https://candlesty.com

---

**Candlesty** - Revolutionizing trading with AI-powered solutions for the modern financial world. 