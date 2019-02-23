CREATE TABLE IF NOT EXISTS results_test_suites
(
  id            uuid                     not null primary key,
  submission_id uuid                     not null,
  name          varchar                  not null,
  duration      numeric                  not null,
  created_at    timestamp with time zone not null default now()
);

ALTER TABLE results_test_suites
  ADD CONSTRAINT fk_results_test_suites_submission FOREIGN KEY (submission_id) REFERENCES submissions (id) ON DELETE CASCADE;

ALTER TABLE results_test_suites
  ADD CONSTRAINT uq_results_test_suites UNIQUE (submission_id, name);
