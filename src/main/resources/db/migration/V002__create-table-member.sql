CREATE TABLE member (
	cpf bpchar(11) NOT NULL,
	"name" varchar(255) NOT NULL,
	inclusion_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT member_pkey PRIMARY KEY (cpf)
);