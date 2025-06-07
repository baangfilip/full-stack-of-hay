# full-stack-of-hay

Havent had time to continue implement any frontend yet.  

Experiment fullstack Java, Svelte, Kafka and MSSQL. Collect and view temperature and humidity data from sensors. 

Run with 
```
podman compose -f docker-compose-development.yaml up
```
and then 
```
mvn jetty:run pom.xml
```

Frontend not started yet. 


For python requirements.txt in virtual env:
```
pipreqs --ignore bin,etc,include,lib,lib64
```