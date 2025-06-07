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
      sensorid CHAR(36),
      temperature FLOAT,
      sensordate DATETIME,
      createdat DATETIME DEFAULT GETDATE()
    );
END; 

IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_SCHEMA = 'fullstackofhay' AND TABLE_NAME = 'voltage'
)
BEGIN
    CREATE TABLE voltage(
      id INT IDENTITY(1,1) PRIMARY KEY,
      sensorid CHAR(36),
      voltage FLOAT,
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
WHERE TABLE_NAME = 'voltage';


INSERT INTO temperature (temperature, sensorid, sensordate) VALUES (21.3, 'a32e2f4f-8698-493b-8c86-3b3b6e6c4195', '2025-05-08 12:47:49');
INSERT INTO temperature (temperature, sensorid, sensordate) VALUES (22.4, 'a32e2f4f-8698-493b-8c86-3b3b6e6c4195', '2025-05-08 12:47:50');
INSERT INTO temperature (temperature, sensorid, sensordate) VALUES (23.1, '2625d89a-e23d-4370-9b3d-3ab667063143', '2025-05-08 12:47:51');
INSERT INTO temperature (temperature, sensorid, sensordate) VALUES (19.9, '2625d89a-e23d-4370-9b3d-3ab667063143', '2025-05-08 12:47:46');

SELECT * FROM temperature;

INSERT INTO voltage (voltage, sensorid, sensordate) VALUES (21.3, 'a32e2f4f-8698-493b-8c86-3b3b6e6c4195', '2025-05-08 12:47:49');
INSERT INTO voltage (voltage, sensorid, sensordate) VALUES (22.4, 'a32e2f4f-8698-493b-8c86-3b3b6e6c4195', '2025-05-08 12:47:50');
INSERT INTO voltage (voltage, sensorid, sensordate) VALUES (23.1, '2625d89a-e23d-4370-9b3d-3ab667063143', '2025-05-08 12:47:51');
INSERT INTO voltage (voltage, sensorid, sensordate) VALUES (19.9, '2625d89a-e23d-4370-9b3d-3ab667063143', '2025-05-08 12:47:46');

SELECT * FROM voltage;