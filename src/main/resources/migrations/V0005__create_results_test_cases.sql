CREATE TABLE IF NOT EXISTS results_test_cases
(
  id              uuid                     not null primary key,
  suite_id        uuid                     not null,
  duration        numeric                  not null,
  is_errored      boolean                  not null,
  is_failed       boolean                  not null,
  is_skipped      boolean                  not null,
  is_successful   boolean                  not null,
  failure_message varchar                  null,
  name            varchar                  not null,
  created_at      timestamp with time zone not null default now()
);

ALTER TABLE results_test_cases
  ADD CONSTRAINT fk_results_test_cases_suite FOREIGN KEY (suite_id) REFERENCES results_test_suites (id) ON DELETE CASCADE;

ALTER TABLE results_test_cases
  ADD CONSTRAINT uq_results_test_cases UNIQUE (suite_id, name);

CREATE INDEX ix_results_test_cases_suite ON results_test_cases (suite_id);
