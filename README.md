# Spring AI Chatbot REST Application

This is a simple Spring AI Chatbot REST application that uses the OpenAI API to answer questions.
It demonstrates how to monitor Spring AI Chatbot using OpenTelemetry and OPIK server for observability and tracing.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Manual Testing](#manual-testing)
- [API Endpoints](#api-endpoints)
- [Monitoring and Observability](#monitoring-and-observability)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+** for dependency management and building
- **OpenAI API Key** - Sign up at [OpenAI Platform](https://platform.openai.com/)
- **OPIK API Key** - Sign up at [Comet OPIK](https://www.comet.com/opik)

## Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd springAI-otel
```

### 2. Verify Java Installation
```bash
java --version
```
Ensure you have Java 21 or higher installed.

### 3. Verify Maven Installation
```bash
mvn --version
```

### 4. Install Dependencies
```bash
mvn clean install
```

## Configuration

### Environment Variables

The application requires the following environment variables to be set:

#### Required Variables

- **OPENAI_API_KEY**: Your OpenAI API key
- **OTEL_EXPORTER_OTLP_ENDPOINT**: OPIK OpenTelemetry endpoint
- **OTEL_EXPORTER_OTLP_HEADERS**: Authorization headers for OPIK


#### Setting Environment Variables: Using Cloud OPIK (Comet)

**On macOS/Linux:**
```bash
export OPENAI_API_KEY="sk-your-openai-api-key-here"
export OTEL_EXPORTER_OTLP_ENDPOINT="https://www.comet.com/opik/api/v1/private/otel"
export OTEL_EXPORTER_OTLP_HEADERS="Authorization=<your-opik-api-key>,Comet-Workspace=default,projectName=<your-project-name>"
```

**On Windows (Command Prompt):**
```cmd
set OPENAI_API_KEY=sk-your-openai-api-key-here
set OTEL_EXPORTER_OTLP_ENDPOINT=https://www.comet.com/opik/api/v1/private/otel
set OTEL_EXPORTER_OTLP_HEADERS=Authorization=<your-opik-api-key>,Comet-Workspace=default,projectName=<your-project-name>
```

**On Windows (PowerShell):**
```powershell
$env:OPENAI_API_KEY="sk-your-openai-api-key-here"
$env:OTEL_EXPORTER_OTLP_ENDPOINT="https://www.comet.com/opik/api/v1/private/otel"
$env:OTEL_EXPORTER_OTLP_HEADERS="Authorization=<your-opik-api-key>,Comet-Workspace=default,projectName=<your-project-name>"
```

#### Setting Environment Variables: Local OPIK Server

If you're running OPIK locally on port 8080, use these environment variables instead:

**On macOS/Linux:**
```bash
export OPENAI_API_KEY="sk-your-openai-api-key-here"
export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:8080/v1/private/otel"
export OTEL_EXPORTER_OTLP_HEADERS="Comet-Workspace=default,projectName=<your-project-name>"
```

**On Windows (Command Prompt):**
```cmd
set OPENAI_API_KEY=sk-your-openai-api-key-here
set OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:8080/v1/private/otel
set OTEL_EXPORTER_OTLP_HEADERS=Comet-Workspace=default,projectName=<your-project-name>
```

**On Windows (PowerShell):**
```powershell
$env:OPENAI_API_KEY="sk-your-openai-api-key-here"
$env:OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:8080/v1/private/otel"
$env:OTEL_EXPORTER_OTLP_HEADERS="Comet-Workspace=default,projectName=<your-project-name>"
```

**Note**: When using a local OPIK server, you don't need the `Authorization` header in `OTEL_EXPORTER_OTLP_HEADERS`.

### Application Configuration

The application is configured via `src/main/resources/application.yml`:

- **Server Port**: 8085 (customizable)
- **OpenAI Model**: gpt-4o (customizable)
- **Temperature**: 0.7 (controls response creativity)
- **Tracing**: All requests are traced (100% sampling)

## Running the Application

### Method 1: Using Maven Spring Boot Plugin
```bash
mvn spring-boot:run
```

### Method 2: Using JAR File
```bash
mvn clean package
java -jar target/spring-ai-demo-opik-0.0.1-SNAPSHOT.jar
```

### Method 3: Development Mode with Auto-reload
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
```

The application will start on **http://localhost:8085**

## Manual Testing

### 1. Test with curl (GET Request)

**Basic question:**
```bash
curl "http://localhost:8085/api/chat/ask-me?question=What is Spring AI?"
```

**Complex question with URL encoding:**
```bash
curl --get --data-urlencode "question=How to integrate Spring AI with OpenAI for building chatbots?" http://localhost:8085/api/chat/ask-me
```

**Default question (if no parameter provided):**
```bash
curl "http://localhost:8085/api/chat/ask-me"
```

### 2. Test with curl (POST Request)

**Simple POST:**
```bash
curl -X POST \
  -H "Content-Type: text/plain" \
  -d "Explain the benefits of using OpenTelemetry for monitoring" \
  http://localhost:8085/api/chat/ask
```

**POST with JSON (if needed):**
```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '"What are the key features of Spring Boot 3.4?"' \
  http://localhost:8085/api/chat/ask
```

### 3. Test with Postman

#### GET Request:
- **Method**: GET
- **URL**: `http://localhost:8085/api/chat/ask-me`
- **Query Parameters**:
  - Key: `question`
  - Value: `How to integrate Spring AI with OpenAI?`

#### POST Request:
- **Method**: POST
- **URL**: `http://localhost:8085/api/chat/ask`
- **Headers**: `Content-Type: text/plain`
- **Body**: `What is the difference between Spring AI and LangChain?`

### 4. Test with HTTPie
```bash
http GET localhost:8085/api/chat/ask-me question=="What is machine learning?"
```

### 5. Test with Browser

Open your browser and navigate to:
```
http://localhost:8085/api/chat/ask-me?question=Tell me about Spring Framework
```

## API Endpoints

### GET /api/chat/ask-me
- **Description**: Ask a question using query parameter
- **Parameters**:
  - `question` (optional): Your question (defaults to "Tell me a joke")
- **Example**: `/api/chat/ask-me?question=What is AI?`

### POST /api/chat/ask
- **Description**: Ask a question using request body
- **Content-Type**: `text/plain`
- **Body**: Your question as plain text
- **Example**:
  ```
  POST /api/chat/ask
  Content-Type: text/plain

  What is Spring AI?
  ```

## Monitoring and Observability

### OpenTelemetry Integration

The application automatically captures:
- **HTTP requests** and responses
- **OpenAI API calls** and responses
- **Application metrics** and traces
- **Custom spans** for business logic

### Viewing Traces in OPIK

1. Navigate to [Comet OPIK Dashboard](https://www.comet.com/opik)
2. Select your workspace and project
3. View real-time traces and metrics
4. Analyze performance and debugging information

### Health Check

Check application health:
```bash
curl http://localhost:8085/actuator/health
```

## Troubleshooting

### Common Issues

#### 1. Application Won't Start

**Error**: `Failed to configure a DataSource`
- **Solution**: This shouldn't occur with this application as it doesn't use a database

**Error**: `OpenAI API key not found`
- **Solution**: Ensure `OPENAI_API_KEY` environment variable is set correctly

#### 2. OpenTelemetry Issues

**Error**: `Failed to export telemetry data`
- **Solution**: Check your `OTEL_EXPORTER_OTLP_ENDPOINT` and `OTEL_EXPORTER_OTLP_HEADERS` configuration
- **Verify**: Your OPIK API key is valid and has proper permissions

#### 3. Port Already in Use

**Error**: `Port 8085 is already in use`
- **Solution**: Change the port in `application.yml`:
  ```yml
  server:
    port: 8086
  ```

#### 4. OpenAI API Errors

**Error**: `Rate limit exceeded`
- **Solution**: Check your OpenAI usage limits and billing

**Error**: `Invalid API key`
- **Solution**: Verify your OpenAI API key is correct and active

### Logs

To enable debug logging, add to `application.yml`:
```yml
logging:
  level:
    com.comet.opik.examples: DEBUG
    org.springframework.ai: DEBUG
```

### Testing Without OpenTelemetry

To run without telemetry export, set:
```bash
export OTEL_EXPORTER_OTLP_ENDPOINT=""
```

## Additional Information

### Dependencies Used

- **Spring Boot 3.4.3**
- **Spring AI 1.0.0**
- **OpenTelemetry Instrumentation**
- **Micrometer Tracing**
- **Spring Boot Actuator**

### Development

For development purposes, you can modify the OpenAI model and parameters in `application.yml`:
```yml
spring:
  ai:
    openai:
      chat:
        options:
          model: gpt-3.5-turbo  # or gpt-4, gpt-4o-mini
          temperature: 0.3      # Lower for more deterministic responses
```