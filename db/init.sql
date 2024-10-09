CREATE TABLE user (
    id SERIAL PRIMARY KEY,
    email character varying(255) NOT NULL UNIQUE,
    username character varying(255) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);