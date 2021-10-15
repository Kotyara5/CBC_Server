CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login CHARACTER VARYING(30),
    password CHARACTER VARYING(255),
    name CHARACTER VARYING(30),
    email CHARACTER VARYING(30),
    role CHARACTER VARYING(30),
    enabled BOOLEAN,
    locked BOOLEAN
);
CREATE TABLE refreshtoken (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    token CHARACTER VARYING(255) UNIQUE,
    expiry_date timestamp without time zone
);
CREATE TABLE friends (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE dialogs (
    id BIGSERIAL PRIMARY KEY,
    name CHARACTER VARYING(30),
    creator_id BIGINT REFERENCES users(id)
);
CREATE TABLE usersdialogs (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    dialogs_id BIGINT REFERENCES dialogs(id) ON DELETE CASCADE
);
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    dialogs_id BIGINT REFERENCES dialogs(id) ON DELETE CASCADE,
    sender_id BIGINT REFERENCES users(id),
    content CHARACTER VARYING(255),
    timestamp timestamp without time zone,
    type CHARACTER VARYING(30),
    status CHARACTER VARYING(30)
);
