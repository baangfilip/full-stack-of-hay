# full-stack-of-hay
Experiment fullstack Java, Svelte, Kafka and MSSQL.

Collect and view temperature and humidity data from sensors.

Run with 
```
podman compose -f docker-compose-development.yaml up
```
and then 
```
mvn jetty:run pom.xml
```

Frontend not started yet. 