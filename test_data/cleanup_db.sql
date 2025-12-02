-- Drop the dependent view FIRST.
DROP VIEW IF EXISTS operational_view;

-- Drop the tables.
DROP TABLE IF EXISTS progress_records;
DROP TABLE IF EXISTS adults;
DROP TABLE IF EXISTS children;