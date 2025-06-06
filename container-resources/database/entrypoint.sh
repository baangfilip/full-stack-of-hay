#!/bin/bash
/opt/mssql/bin/sqlservr &

echo "Waiting for SQL Server to start..."
until /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P $MSSQL_SA_PASSWORD -C -Q "SELECT 1" 
do
  echo "Waiting for SQL Server to start..."
  sleep 1
done

echo "Running initalize-db.sql..."
/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P $MSSQL_SA_PASSWORD -C -i /scripts/initalize-db.sql

# Keep the container alive
wait
