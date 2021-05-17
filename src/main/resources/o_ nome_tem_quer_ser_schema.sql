drop table if exists User_TB;

CREATE TABLE User_TB (
    identification   VARCHAR(400) NOT NULL ,
    nome VARCHAR(400) NOT NULL,
    email VARCHAR(400) NOT NULL,
    PRIMARY KEY (identification)
);