-- insert password column
ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT 'user';

-- insert roles column
ALTER TABLE users ADD COLUMN role VARCHAR(255) NOT NULL DEFAULT 'USER';

-- insert status column
ALTER TABLE users ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE';