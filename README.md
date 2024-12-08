# StudyFlow
StudyFlow is a web application designed to help students manage their academic schedules, track their grades, and organize their study materials. It provides tools for creating and viewing schedules, tracking grades, and organizing study materials in one place. With a user-friendly interface and cross-platform accessibility, StudyFlow aims to simplify academic management for students.

## Table of Contents
- [Installation](#installation)
- [Backend Server with Docker](#backend-server-with-docker)
   - [Backend](#backend)
   - [Frontend](#frontend)
- [Class Diagrams](#class-diagrams)
- [License](#license)


## Backend Server with Docker
This guide describes how to build and run the Docker image for the StudyFlow Java backend server locally.

## Prerequisites
- [Docker](https://www.docker.com/) must be installed.
- The `Dockerfile` is already present in the `studyflow/backend` directory.


### Backend

1. Open a terminal and navigate to the backend directory:
    ```sh
    cd studyflow/backend
    ```

2. Build the Docker image:
    ```sh
    docker build -t studyflow-backend .
    ```
   - `-t studyflow-backend`: Assigns a name to the Docker image.
   - `.`: Refers to the current directory where the Dockerfile is located.

3. Start the Docker container:
    ```sh
    docker run -d -p 8080:8080 --name studyflow-backend studyflow-backend
    ```
   - `-d`: Runs the container in the background.
   - `-p 8080:8080`: Maps host port 8080 to container port 8080.
   - `--name studyflow-backend`: Assigns the name `studyflow-backend` to the container.
   - `studyflow-backend`: Refers to the created Docker image.

4. The backend server is now accessible at `http://localhost:8080`.

### Frontend

1. Navigate to the frontend directory:
    ```sh
    cd studyflow/frontend
    ```

2. Build the Docker image:
    ```sh
    docker build -t studyflow-frontend .
    ```

3. Start the Docker container:
    ```sh
    docker run -d -p 3000:3000 --name studyflow-frontend studyflow-frontend
    ```

4. The frontend is now accessible at `http://localhost:3000`.

## Class Diagrams

For detailed class diagrams, please refer to the [backend architecture documentation](https://github.com/mvoemel/studyflow/blob/dev/docs/backendArchitecture.md).

## Testing Concept

For detailed information on the testing strategy and test cases, please refer to the [testing concept documentation](https://github.com/mvoemel/studyflow/blob/dev/docs/testingConcept/Test_Cases.md).


## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.