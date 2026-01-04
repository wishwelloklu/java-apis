-- 1. We must remove existing data because IDs cannot be converted cleanly from Integer to ISO String
TRUNCATE TABLE packages CASCADE;

-- 2. Alter the column type
ALTER TABLE packages ALTER COLUMN id TYPE VARCHAR(255);

ALTER TABLE packages ALTER COLUMN id DROP DEFAULT;

-- 3. Drop the Sequence if it exists (since we won't use auto-increment anymore)
DROP SEQUENCE IF EXISTS packages_id_seq;