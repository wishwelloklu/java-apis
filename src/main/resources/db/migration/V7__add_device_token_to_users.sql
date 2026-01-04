ALTER TABLE users ADD COLUMN device_token VARCHAR(50);

UPDATE users SET device_token = '' WHERE device_token IS NULL;

ALTER TABLE users ALTER COLUMN device_token SET NOT NULL;