# StudyFlow

StudyFlow is a web application designed to help students manage their academic schedules, track their grades, and organize their study materials.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Class Diagrams](#class-diagrams)
- [License](#license)

## Features

- **User authentication and authorization**: Secure login and registration system to manage user access and permissions.
- **Manage degrees, semesters, modules, and marks**: Comprehensive management of academic structures, including degrees, semesters, modules, and marks.
- **Schedule management**: Tools to create, update, and manage academic schedules.
- **Appointment management**: Functionality to schedule, view, and manage appointments.
- **Rating system for modules**: System to rate and review modules based on complexity, understanding, and time required.

## Installation

To install and run the project locally, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/kryezleo/studyflow.git
    cd studyflow
    ```

2. Set up the database:
    ```sh
    psql -f init.sql
    ```

3. Build the project using Gradle:
    ```sh
    ./gradlew build
    ```

4. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## Usage

Once the application is running, you can access it at `http://localhost:8080`.


## Class Diagrams

For detailed class diagrams, please refer to the [backend architecture documentation](https://github.com/mvoemel/studyflow/blob/dev/docs/backendArchitecture.md).

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.