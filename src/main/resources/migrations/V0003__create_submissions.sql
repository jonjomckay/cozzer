CREATE TABLE IF NOT EXISTS submissions
(
  id         uuid not null primary key,
  project_id uuid not null,
  created_at timestamp with time zone default now()
);

ALTER TABLE submissions
  ADD CONSTRAINT fk_submissions_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE;
