# File Storage Application

A secure file storage service built with Spring Boot that provides encrypted file upload and download capabilities using MongoDB GridFS.

## Features

- **Secure File Storage**: Files are encrypted using AES-128 encryption before storage
- **User Management**: Complete user authentication and authorization system
- **MongoDB GridFS**: Efficient storage of large files using MongoDB GridFS
- **RESTful API**: Clean REST endpoints for file operations
- **Metadata Tracking**: Comprehensive file metadata including size, type, owner, and encryption status
- **User-based Access Control**: Files are associated with specific users for secure access
- **Large File Support**: Supports files up to 200MB in size
- **JWT Authentication**: Secure authentication using JSON Web Tokens

## Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data MongoDB**
- **MongoDB GridFS** for file storage
- **JWT** for authentication
- **AES-128 Encryption** for file security
- **Lombok** for boilerplate reduction
- **Log4j2** for logging
- **AspectJ** for cross-cutting concerns

## Prerequisites

- Java 17 or higher
- MongoDB 4.0 or higher
- Maven 3.6 or higher

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd file-storage
   ```

2. **Configure MongoDB**
   - Install MongoDB and ensure it's running on localhost:27017
   - Create a database named `storage`
   - Create an admin user with credentials: `admin/password`

3. **Configure Application Properties**
   
   Update `src/main/resources/application.properties`:
   
   ```properties
   # Application Configuration
   spring.application.name=file storage
   server.port=8081
   
   # MongoDB Configuration
   spring.data.mongodb.uri=mongodb://admin:password@localhost:27017/storage?authSource=admin
   spring.data.mongodb.database=storage
   spring.data.mongodb.gridfs.bucket=test
   
   # File Upload Configuration
   spring.servlet.multipart.max-file-size=200MB
   spring.servlet.multipart.max-request-size=200MB
   
   # JWT Configuration
   application.security.jwt.secret-key=your-secret-key-here
   application.security.jwt.expiration=86400000
   
   # PGP Keys (if using PGP encryption)
   secret.key.public=path/to/public.pgp
   secret.key.private=path/to/private.pgp
   secret.key.passkey=your-passkey
   ```

4. **Build the application**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8081`

## API Documentation

### File Upload

**Endpoint**: `POST /api/document/{user}`

**Description**: Upload a file for a specific user

**Request**:
- **Path Parameter**: `user` - User identifier
- **Body**: Multipart form data with `file` field

**Response**:
```json
{
  "id": "64a1b2c3d4e5f6789abcdef0",
  "fileName": "example.pdf"
}
```

**Example**:
```bash
curl -X POST \
  http://localhost:8081/api/document/user123 \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/your/file.pdf'
```

### File Download

**Endpoint**: `GET /api/document/{user}/{id}`

**Description**: Download a file by ID for a specific user

**Request**:
- **Path Parameters**: 
  - `user` - User identifier
  - `id` - File identifier

**Response**: Binary file data with appropriate headers

**Example**:
```bash
curl -X GET \
  http://localhost:8081/api/document/user123/64a1b2c3d4e5f6789abcdef0 \
  -o downloaded-file.pdf
```

## Security Features

### File Encryption
- All files are automatically encrypted using AES-128 encryption before storage
- Encryption keys are generated dynamically for each application instance
- Files are decrypted on-the-fly during download

### User Access Control
- Files are associated with specific users
- Only the file owner can download their files
- Unauthorized access attempts are rejected

### JWT Authentication
- JWT tokens are used for secure authentication
- Configurable token expiration (default: 24 hours)
- Tokens include user information and permissions

## File Storage Details

### GridFS Integration
- Uses MongoDB GridFS for efficient large file storage
- Files are stored in chunks for optimal performance
- Metadata is stored alongside file content

### File Metadata
Each stored file includes comprehensive metadata:
- **fileName**: Original filename
- **fileSize**: File size in bytes
- **fileType**: MIME type
- **owner**: User ID who uploaded the file
- **isEncrypted**: Encryption status (always true)
- **isCompressed**: Compression status
- **version**: File version number
- **duplicateCount**: Number of duplicates

## User Management

The application includes a complete user management system:

### User Features
- User registration and authentication
- Role-based access control
- Account status management (enabled, locked, expired)
- Unique bucket assignment per user

### User Roles
- Configurable role system
- Role-based permissions for file access

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/file/storage/
│   │   ├── configuration/     # Application configuration
│   │   ├── dao/              # Data Access Objects
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── helper/           # Utility classes
│   │   ├── mapper/           # Object mapping
│   │   ├── pojo/             # Plain Old Java Objects
│   │   ├── repository/       # Data repositories
│   │   ├── service/          # Business logic
│   │   └── web/rest/         # REST controllers
│   └── resources/
│       ├── application.properties
│       └── secret/           # PGP keys
└── test/                     # Test classes
```

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package -Pprod
```

## Configuration

### Environment Profiles
- **dev**: Development profile with debug logging
- **prod**: Production profile with optimized settings

### Key Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Application port | 8081 |
| `spring.data.mongodb.uri` | MongoDB connection string | mongodb://admin:password@localhost:27017/storage?authSource=admin |
| `spring.servlet.multipart.max-file-size` | Maximum file upload size | 200MB |
| `application.security.jwt.expiration` | JWT token expiration time | 86400000 (24 hours) |

## Monitoring and Logging

### Logging Configuration
- Uses Log4j2 for structured logging
- Debug logging for AOP and security components
- Request/response logging for file operations

### Health Monitoring
The application includes Spring Boot Actuator endpoints for monitoring:
- `/actuator/health` - Application health status
- `/actuator/info` - Application information

## Troubleshooting

### Common Issues

1. **MongoDB Connection Issues**
   - Verify MongoDB is running
   - Check connection credentials in application.properties
   - Ensure the `storage` database exists

2. **File Upload Failures**
   - Check file size limits (max 200MB)
   - Verify user exists in the system
   - Check disk space and MongoDB storage

3. **Authentication Issues**
   - Verify JWT configuration
   - Check token expiration settings
   - Ensure user credentials are correct

### Error Codes
- **404**: File or user not found
- **401**: Unauthorized access
- **413**: File too large
- **500**: Internal server error (check logs)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please contact the development team or create an issue in the repository.