ALTER TABLE submissions ADD COLUMN external_key varchar;

ALTER TABLE submissions ADD CONSTRAINT uq_submissions_external_key UNIQUE (project_id, external_key);
