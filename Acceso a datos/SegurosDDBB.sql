CREATE DATABASE SegurosDDBB
GO
USE SegurosDDBB
GO

CREATE TABLE Seguro(
	idSeguro int identity(1, 1),
    nif varchar(9),
    nombre varchar(30),
    apellido1 varchar(30),
    apellido2 varchar(30),
    edad tinyint,
    numHijos tinyint,
    fechaCreacion datetime,
	CONSTRAINT PK_Seguro primary key
	(idSeguro)
)

CREATE TABLE AsistenciaMedica(
	idAsistenciaMedica int identity(1, 1),
	idSeguro int,
	breveDescripcion varchar(30),
	lugar varchar(30),
	CONSTRAINT PK_AsistenciaMedica primary key
	(idAsistenciaMedica),
	CONSTRAINT FK_AsistenciaMedica_Seguro foreign key
	(idSeguro) references Seguro(idSeguro) ON UPDATE 
	CASCADE ON DELETE CASCADE
)

SELECT * FROM Seguro
SELECT * FROM AsistenciaMedica