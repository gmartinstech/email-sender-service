# Email Sender Service

A microservice for sending emails built with Quarkus.

## Features

- RESTful API for sending emails
- Support for HTML and plain text emails
- Support for CC and BCC recipients
- Validation of email addresses and required fields
- Asynchronous email sending
- Health checks
- Docker support

## Prerequisites

- JDK 17+
- Maven 3.8.1+
- Docker (optional, for containerization)

## Building the Application

### JVM Build

```bash
mvn clean package
```

### Native Build

```bash
mvn clean package -Dnative
```

## Running the Application

### Development Mode

```bash
mvn quarkus:dev
```

### JVM Mode

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

### Native Mode

```bash
./target/email-sender-service-1.0.0-SNAPSHOT-runner
```

## Configuration

The application can be configured using the `application.properties` file or environment variables:

- `quarkus.mailer.from` - Default sender email address
- `quarkus.mailer.host` - SMTP server host
- `quarkus.mailer.port` - SMTP server port
- `quarkus.mailer.ssl` - Enable SSL
- `quarkus.mailer.username` - SMTP username
- `quarkus.mailer.password` - SMTP password

## API Endpoints

### Send Email

```
POST /email/send
Content-Type: application/json

{
  "to": ["recipient@example.com"],
  "cc": ["cc@example.com"],
  "bcc": ["bcc@example.com"],
  "subject": "Hello",
  "body": "Hello World",
  "html": false
}
```

## Docker

### Building the Container

```bash
docker build -f src/main/docker/Dockerfile.jvm -t email-sender-service .
```

### Running the Container

```bash
docker run -i --rm -p 8081:8081 email-sender-service
```