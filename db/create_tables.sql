
\c studyflow_db;

CREATE TABLE "Users" (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    avatarUrl TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE "Degree" (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    description TEXT
);

CREATE TABLE "UserDegree" (
    userId VARCHAR(255),
    degreeId VARCHAR(255),
    PRIMARY KEY (userId, degreeId),
    FOREIGN KEY (userId) REFERENCES "Users"(id),
    FOREIGN KEY (degreeId) REFERENCES "Degree"(id)
);

CREATE TABLE "Semester" (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    degreeId VARCHAR(255),
    FOREIGN KEY (degreeId) REFERENCES "Degree"(id)
);

CREATE TABLE "Module" (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    ects NUMERIC,
    semesterId VARCHAR(255),
    FOREIGN KEY (semesterId) REFERENCES "Semester"(id)
);

CREATE TABLE "Mark" (
    id VARCHAR(255) PRIMARY KEY,
    mark NUMERIC,
    courseWorkMark NUMERIC,
    courseWorkPercentage NUMERIC,
    moduleId VARCHAR(255),
    FOREIGN KEY (moduleId) REFERENCES "Module"(id)
);

CREATE TABLE "Rating" (
    id VARCHAR(255) PRIMARY KEY,
    complexity NUMERIC,
    understanding NUMERIC,
    time NUMERIC,
    moduleId VARCHAR(255),
    FOREIGN KEY (moduleId) REFERENCES "Module"(id)
);

CREATE TABLE "Schedule" (
    id VARCHAR(255) PRIMARY KEY,
    content TEXT,
    semesterId VARCHAR(255),
    FOREIGN KEY (semesterId) REFERENCES "Semester"(id)
);
