IF EXISTS (SELECT name FROM sys.databases WHERE name = N'fullstackofhay')
BEGIN
    DROP DATABASE fullstackofhay;
END;

CREATE DATABASE fullstackofhay;
GO
USE fullstackofhay;

IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_SCHEMA = 'fullstackofhay' AND TABLE_NAME = 'temperature'
)
BEGIN
    CREATE TABLE temperature(
      id INT IDENTITY(1,1) PRIMARY KEY,
      temperature FLOAT,
      sensordate DATETIME,
      createdat DATETIME DEFAULT GETDATE()
    );
END; 

IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_SCHEMA = 'fullstackofhay' AND TABLE_NAME = 'humidity'
)
BEGIN
    CREATE TABLE humidity(
      id INT IDENTITY(1,1) PRIMARY KEY,
      humidity FLOAT,
      sensordate DATETIME,
      createdat DATETIME DEFAULT GETDATE()
    );
END; 

SELECT TABLE_SCHEMA, TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE';

SELECT 
    TABLE_SCHEMA,
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'temperature';

SELECT 
    TABLE_SCHEMA,
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'humidity';


INSERT INTO temperature (temperature, sensordate) VALUES (21.3, '2025-05-08 12:47:49');
INSERT INTO temperature (temperature, sensordate) VALUES (22.4, '2025-05-08 12:47:50');
INSERT INTO temperature (temperature, sensordate) VALUES (23.1, '2025-05-08 12:47:51');
INSERT INTO temperature (temperature, sensordate) VALUES (19.9, '2025-05-08 12:47:46');

SELECT * FROM temperature;