CREATE TABLE vote (
	id serial4 NOT NULL,
	cpf varchar(11) NOT NULL,
	choice varchar NOT NULL,
	agenda_id uuid NOT NULL,
	CONSTRAINT vote_pkey PRIMARY KEY (id),
	CONSTRAINT fk_agenda FOREIGN KEY (agenda_id) REFERENCES public.agenda(agenda_id) ON DELETE CASCADE
);