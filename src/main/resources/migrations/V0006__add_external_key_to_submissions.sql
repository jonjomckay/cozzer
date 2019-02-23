ALTER TABLE submissions ADD COLUMN IF NOT EXISTS external_key varchar;

ALTER TABLE submissions DROP CONSTRAINT IF EXISTS uq_submissions_external_key;
ALTER TABLE submissions ADD CONSTRAINT uq_submissions_external_key UNIQUE (project_id, external_key);
