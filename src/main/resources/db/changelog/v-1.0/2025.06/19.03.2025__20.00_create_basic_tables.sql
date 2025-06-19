CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255) NOT NULL UNIQUE,
    username   VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    is_active  BOOLEAN     DEFAULT TRUE,
    role       VARCHAR(50) DEFAULT 'USER'
);

CREATE TABLE posts
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    content VARCHAR(1000)
);