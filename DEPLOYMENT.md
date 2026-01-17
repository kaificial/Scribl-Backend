# Deployment Guide - Scribl Backend

This document explains how to deploy the Scribl backend application.

## 1. AWS Elastic Beanstalk (Recommended)

The project is pre-configured to deploy automatically to AWS Elastic Beanstalk via GitHub Actions.

### Prerequisites:
- An AWS account.
- An Elastic Beanstalk application named `Scribl-Backend`.
- An environment named `Scribl-Backend-env` (Platform: Java 17).
- A PostgreSQL database (RDS) or a managed DB.

### Setup GitHub Secrets:
Go to your GitHub repository -> Settings -> Secrets and variables -> Actions and add:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

### Triggering Deployment:
- Push any change to the `main` branch.
- Or manually trigger the "Build JAR for AWS Deployment" workflow from the GitHub Actions tab.

---

## 2. Docker Deployment

You can run the application anywhere that supports Docker (Heroku, AWS App Runner, DigitalOcean, etc.).

### Local Testing:
To test the production-like environment (App + Postgres) locally:
```bash
docker-compose up --build
```
The app will be available at `http://localhost:8080`.

### Building the Image:
```bash
docker build -t scribl-backend .
```

---

## 3. Environment Variables

The application requires the following environment variables in production:

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_HOST` | Database Host | `my-db.xxxxxxxx.us-east-1.rds.amazonaws.com` |
| `DB_NAME` | Database Name | `scribl_db` |
| `DB_USER` | Database Username | `admin` |
| `DB_PASSWORD` | Database Password | `securepassword` |
| `CORS_ALLOWED_ORIGINS` | Allowed Frontend URLs | `https://my-frontend.vercel.app` |
| `SPRING_PROFILES_ACTIVE` | Active Profile | `prod` |

---

## 4. Manual Deployment

If you want to build and run manually:
1. Build the JAR:
   ```bash
   mvn clean package -DskipTests
   ```
2. Run the JAR:
   ```bash
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```
