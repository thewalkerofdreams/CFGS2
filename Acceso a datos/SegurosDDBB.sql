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

SELECT * FROM Seguro