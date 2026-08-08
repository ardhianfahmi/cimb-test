-- add email and phone number to users
ALTER TABLE users
    ADD COLUMN email VARCHAR(100),
    ADD COLUMN phone_number VARCHAR(30);

-- fill old rows (example supervisor) so NOT NULL can be applied
UPDATE users
SET email = username || '@example.com',
    phone_number = '08000000000'
WHERE email IS NULL OR phone_number IS NULL;

ALTER TABLE users
    ALTER COLUMN email SET NOT NULL,
    ALTER COLUMN phone_number SET NOT NULL;

CREATE UNIQUE INDEX uq_users_email ON users (email);
