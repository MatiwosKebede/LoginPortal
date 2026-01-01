-- Sample SQL commands to test
USE authdb;

-- Show all users
SELECT * FROM users;

-- Add a new user manually
INSERT INTO users (username, password_hash) 
VALUES ('john', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

-- Update a user's password
UPDATE users SET password_hash = 'new_hash_here' WHERE username = 'admin';

-- Delete a user
DELETE FROM users WHERE username = 'test';
