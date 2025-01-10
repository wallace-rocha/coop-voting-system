CREATE TABLE agenda (
	agenda_id uuid DEFAULT gen_random_uuid() NOT NULL,
	subject varchar(255) NOT NULL,
	inclusion_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	voting_start_date timestamp NULL,
	voting_end_date timestamp NULL,
	CONSTRAINT agenda_pkey PRIMARY KEY (agenda_id)
);