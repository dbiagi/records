CREATE DATABASE records;
CREATE USER records WITH password 'J93jd!H32u1';
GRANT ALL PRIVILEGES ON database records TO records;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE client_record (
	id uuid DEFAULT uuid_generate_v4(),
	description TEXT,
	value decimal(14,2),
	created_at timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE client_record_history (
	id uuid,
	description TEXT,
	value decimal(14,2),
	created_at timestamp,
	PRIMARY KEY (id)
);
