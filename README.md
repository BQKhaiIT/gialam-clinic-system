# Gia Lam Clinic Management System

A modern fullstack clinic management system built with Vue 3, Spring Boot and PostgreSQL.
The project focuses on digitizing clinic workflows such as patient management, appointment scheduling, medical examination and prescription management.

This system is designed as a portfolio-grade MVP with clean architecture, realtime support and Docker deployment.

---

# Preview

## Main Features

* JWT Authentication & Authorization
* Role-based Access Control
* Patient Management
* Appointment Scheduling
* Medical Records
* Prescription Management
* Medicine Management
* Dashboard Statistics
* Realtime Notification with WebSocket
* Dockerized Environment

---

# Tech Stack

## Frontend

* Vue 3
* Vite
* Vue Router
* Pinia
* Axios
* Element Plus
* Chart.js

---

## Backend

* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* PostgreSQL
* WebSocket (STOMP)

---

## DevOps

* Docker
* Docker Compose
* Nginx

---

# System Architecture

```txt
Frontend (Vue 3)
        ↓
 REST API + WebSocket
        ↓
Backend (Spring Boot)
        ↓
 PostgreSQL
```

---

# Roles

| Role   | Description                              |
| ------ | ---------------------------------------- |
| ADMIN  | Manage users, medicines and dashboard    |
| DOCTOR | Manage medical records and prescriptions |
| STAFF  | Manage patients and appointments         |

---

# Core Workflow

## 1. Authentication

Users login using username and password.

The backend validates credentials and generates JWT tokens.

Frontend stores token and automatically attaches it to API requests.

---

## 2. Patient Management

Staff can:

* Create patient
* Search patient
* Update patient information

---

## 3. Appointment Management

Staff creates appointments and assigns doctors.

Doctors can:

* View assigned appointments
* Update appointment status

Appointment statuses:

* PENDING
* CONFIRMED
* COMPLETED
* CANCELLED

---

## 4. Medical Examination

Doctors can:

* View patient information
* Create medical records
* Add diagnosis
* Add treatment
* Add examination notes

---

## 5. Prescription Management

Doctors create prescriptions after examination.

The system supports:

* Multiple medicines in one prescription
* Quantity calculation
* Total amount calculation

Prescription statuses:

* PENDING
* DONE

Payment statuses:

* UNPAID
* PAID

---

## 6. Dashboard

Admin dashboard includes:

* Total patients
* Total appointments
* Total prescriptions
* Medicine statistics
* Appointment charts

---

# Database Design

## Main Tables

* users
* patients
* appointments
* medical_records
* medicines
* prescriptions
* prescription_details

---

# Database Schema

```dbml
Table users {
  id bigint [pk, increment]

  username varchar [unique]
  password varchar

  full_name varchar
  phone varchar
  email varchar

  role varchar
  is_active boolean

  created_at timestamp
  updated_at timestamp
}

Table patients {
  id bigint [pk, increment]

  full_name varchar
  dob date
  gender varchar

  phone varchar
  address varchar

  blood_type varchar
  allergies text

  created_at timestamp
}

Table appointments {
  id bigint [pk, increment]

  patient_id bigint [ref: > patients.id]
  doctor_id bigint [ref: > users.id]

  appointment_date date
  appointment_time time

  reason text
  notes text

  status varchar

  created_by bigint [ref: > users.id]

  created_at timestamp
}

Table medical_records {
  id bigint [pk, increment]

  appointment_id bigint [ref: > appointments.id]

  patient_id bigint [ref: > patients.id]
  doctor_id bigint [ref: > users.id]

  diagnosis text
  treatment text

  notes text

  created_at timestamp
}

Table medicines {
  id bigint [pk, increment]

  name varchar
  category varchar

  unit varchar

  stock_quantity int

  price decimal

  instruction text

  is_active boolean

  created_at timestamp
}

Table prescriptions {
  id bigint [pk, increment]

  medical_record_id bigint [ref: > medical_records.id]

  total_amount decimal

  status varchar
  payment_status varchar

  created_at timestamp
}

Table prescription_details {
  id bigint [pk, increment]

  prescription_id bigint [ref: > prescriptions.id]
  medicine_id bigint [ref: > medicines.id]

  quantity int

  dosage varchar
  frequency varchar
  duration varchar

  unit_price decimal
  subtotal decimal
}
```

---

# Project Structure

## Frontend Structure

```txt
frontend/
 ├── src/
 │    ├── api/
 │    ├── assets/
 │    ├── components/
 │    ├── layouts/
 │    ├── pages/
 │    ├── router/
 │    ├── stores/
 │    ├── services/
 │    ├── websocket/
 │    └── utils/
 │
 ├── package.json
 └── vite.config.js
```

---

## Backend Structure

```txt
backend/
 ├── src/main/java/com/gialamclinic/
 │    ├── config/
 │    ├── controller/
 │    ├── dto/
 │    ├── entity/
 │    ├── repository/
 │    ├── security/
 │    ├── service/
 │    ├── websocket/
 │    └── utils/
 │
 ├── src/main/resources/
 │    └── application.yml
 │
 ├── Dockerfile
 └── pom.xml
```

---

# API Convention

## Base URL

```txt
/api/v1
```

---

## Response Format

```json
{
  "success": true,
  "message": "Success",
  "data": {}
}
```

---

# Realtime Notification

The system uses Spring WebSocket + STOMP for realtime updates.

Realtime events:

* New appointment created
* New prescription created
* Appointment status updated

---

# Environment Variables

## Frontend

```env
VITE_API_URL=http://localhost:8080/api/v1
```

---

## Backend

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gialam_clinic
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=123456

JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000
```

---

# Local Development Setup

## 1. Clone Project

```bash
git clone https://github.com/your-username/gialam-clinic-system.git
```

---

## 2. Setup Backend

```bash
cd backend
```

Install dependencies and run:

```bash
./mvnw spring-boot:run
```

Backend runs at:

```txt
http://localhost:8080
```

---

## 3. Setup Frontend

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Run development server:

```bash
npm run dev
```

Frontend runs at:

```txt
http://localhost:5173
```

---

# Docker Setup

## Run Entire System

```bash
docker-compose up --build
```

---

## Docker Services

* frontend
* backend
* postgres
* nginx

---

# Features Roadmap

## Current MVP

* Authentication
* Patients
* Appointments
* Medical Records
* Prescriptions
* Dashboard
* Realtime Notification

---

## Future Improvements

* Mobile App
* Payment Gateway
* Export PDF
* Audit Log
* AI Suggestion System
* Advanced Analytics

---

# UI Screens

## Planned Screens

* Login
* Dashboard
* Patients
* Appointments
* Medical Records
* Prescriptions
* Medicines
* Users

---

# Security

The project implements:

* JWT Authentication
* Password Encryption
* Role-based Authorization
* Protected Routes
* API Security with Spring Security

---

# Why This Project Matters

This project simulates a real-world clinic management workflow while keeping the architecture lightweight and scalable.

The goal is not only CRUD operations but also:

* business workflow handling
* role-based systems
* realtime communication
* scalable backend architecture
* modern frontend structure

---

# Contributors

* Khải
* Team Members

---

# License

This project is for educational and portfolio purposes.
