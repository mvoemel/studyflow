# System Architecture

This document provides an overview of the architecture for StudyFlow. The system is divided into three main components: the frontend, backend, and database, each of which serves a distinct role in the overall architecture.

![architecture](/docs/assets/architecture.png)

---

## Components

### 1. **Frontend (JS, HTML, CSS)**

The frontend is responsible for rendering the user interface (UI) and interacting with the backend through various API endpoints. The frontend is built using JavaScript, HTML, and CSS. It provides various pages and routes that allow the user to interact with their profile, course schedules, grades, and degree settings.

**Frontend Routes:**

- `/register`: User registration page.
- `/login`: User login page.
- `/dashboard`: User dashboard showing general information.
- `/schedule`: View and manage the users schedule.
- `/grades`: View grades for different degrees, semesters and modules.
- `/settings/profile`: Manage user profile settings.
- `/settings/degree`: Manage degree settings.
- `/settings/degree/[degreeId]`: View and edit details for a specific degree.
- `/settings/degree/[degreeId]/semester`: Manage semesters within a degree.
- `/settings/degree/[degreeId]/semester/[semesterId]`: Manage specific semester details.
- `/settings/degree/[degreeId]/semester/[semesterId]/module`: Manage modules within a semester.
- `/settings/degree/[degreeId]/semester/[semesterId]/module/[moduleId]`: Manage details of a specific module.

### 2. **Backend (Java)**

The backend is built in Java and serves as the intermediary between the frontend and the database. It exposes a RESTful API that handles user authentication, profile management, and degree management. The backend interacts with the database to retrieve or update data as requested by the frontend.

**Backend Endpoints:**

- **Public Endpoints:**
  - `POST /register`: Register a new user with `{email, username, password}`.
  - `POST /login`: Authenticate user with `{email, password}`.
  - `GET /profile/:id`: Retrieve the profile of a user by their ID.
  - `GET /avatar/:id`: Retrieve the avatar URL of a user by their ID.
- **Private Endpoints (Require Authentication):**
  - `GET /refresh`: Refresh session or token.
  - `GET /user/:id`: Retrieve detailed user information.
  - `PATCH /user/:id`: Update user information `{email, username, password, ...}`.
  - `POST /schedule`: Create a new schedule with `{startDate, endDate, ...}`.
  - `GET /schedule/:id`: Get schedule by ID.
  - `POST /degree`: Create a degree entry for the user `{userId, name, description, ...}`.
  - `GET /degree/:id`: Get degree details by ID.
  - `POST /degree/:id/semester`: Create a semester for a degree with `{name, description, ...}`.
  - `GET /degree/:id/semester/:id`: Get details of a specific semester by ID.
  - `POST /degree/:id/semester/:id/module`: Add a module to a semester with `{name, ects, ...}`.
  - `GET /degree/:id/semester/:id/module/:id`: Retrieve details of a specific module by ID.

### 3. **Database (PostgreSQL)**

The database stores all the data related to users, their degrees, semesters, and modules. It uses PostgreSQL as the relational database management system (RDBMS). The following entities and their relationships are represented in the database:

**Database Entities:**

- **User:**

  - `id`: Unique identifier for the user.
  - `username`: The username of the user.
  - `email`: The user's email (unique).
  - `password`: Hashed user password.
  - `avatarUrl`: URL for the user's profile picture.
  - `degrees`: A list of degrees that belong to the user (`Degree[]`).

- **Degree:**

  - `id`: Unique identifier for the degree.
  - `name`: Name of the degree.
  - `description`: Description of the degree.
  - `semesters`: A list of semesters within the degree (`Semester[]`).

- **Semester:**

  - `id`: Unique identifier for the semester.
  - `name`: Name of the semester.
  - `description`: Description of the semester.
  - `modules`: A list of modules within the semester (`Module[]`).

- **Module:**

  - `id`: Unique identifier for the module.
  - `name`: Name of the module.
  - `description`: Description of the module.
  - `ects`: Number of ECTS credits for the module.
  - `mark`: A mark object representing the student's grade (`Mark`).
  - `rating`: A rating object representing the complexity and time investment of the module (`Rating`).

- **Rating:**

  - `id`: Unique identifier for the rating.
  - `complexity`: Numeric value representing the complexity of the module.
  - `understanding`: Numeric value representing the student's level of understanding.
  - `time`: Numeric value representing the amount of time spent on the module.

- **Mark:**
  - `id`: Unique identifier for the mark.
  - `mark`: Overall mark for the module.
  - `courseWorkMark`: Mark for coursework.
  - `courseWorkPercentage`: Percentage of the mark attributed to coursework.

---

## Data Flow

1. **User Actions (Frontend to Backend):**

   - Users interact with the frontend to perform various actions such as logging in, registering, viewing/editing their profile, and managing their degrees, semesters, and modules.
   - These actions trigger requests to the backend API, which handles the logic and communicates with the database to retrieve or store data.

2. **Backend Processing:**

   - The backend validates the requests and interacts with the database to perform the necessary operations (e.g., create, read, update, or delete records).
   - After processing the requests, the backend returns the results (data or confirmation) to the frontend for display or further interaction.

3. **Data Persistence (Database):**
   - The database ensures that all user data, including profile information, degrees, semesters, modules, and associated ratings and marks, are securely stored and can be retrieved when necessary.

---

## Summary

This system architecture integrates a frontend that handles user interactions with a Java-based backend and a PostgreSQL database for persistent data storage. The system's main purpose is to manage university-related data such as schedules, grades, degrees, semesters, and modules for students, providing a seamless user experience for course management.
