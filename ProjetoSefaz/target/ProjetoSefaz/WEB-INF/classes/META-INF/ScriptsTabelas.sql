-- Scripts para criação do projeto --

CREATE TABLE endereco (
   codigo INT NOT NULL,
   numero INT NOT NULL,
   rua VARCHAR(100) NOT NULL,
   complemento VARCHAR(100),
   PRIMARY KEY(codigo)
);

CREATE SEQUENCE S_ENDERECO 
	MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1 ;

CREATE TABLE pessoa (
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    idade INT NOT NULL,
    sexo VARCHAR(1) NOT NULL,
    senha VARCHAR(20) NOT NULL,
    codigo_endereco INT NOT NULL,
    PRIMARY KEY (cpf)
);

ALTER TABLE pessoa
ADD FOREIGN KEY (codigo_endereco) 
REFERENCES endereco(codigo);