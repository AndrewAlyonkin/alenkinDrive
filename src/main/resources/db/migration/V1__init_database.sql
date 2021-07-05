-- drop users table
DROP TABLE IF EXISTS users;

-- drop events table
DROP TABLE IF EXISTS events;

-- drop files table
DROP TABLE IF EXISTS files;

-- drop sequence
DROP SEQUENCE IF EXISTS global_seq;

-- init sequence
CREATE SEQUENCE global_seq START WITH 10000;

-- init users table
CREATE TABLE users
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    name VARCHAR NOT NULL
);

-- init files table
CREATE TABLE files
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    file_uri VARCHAR NOT NULL,
    size     BIGINT  NOT NULL,
    user_id  BIGINT  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- init events table
CREATE TABLE events
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    download_time TIMESTAMP NOT NULL,
    file_id       BIGINT   NOT NULL,
    user_id       BIGINT   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);

