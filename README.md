# GameGyft API

An api that performs a Visa direct transfer to fund a GameGyft Card.


## Requirements
- install java
- install leiningen


## Running API
```bash
lein run
```

## Creating Jar and Starting the API
```bash
cd gamegyft-api
lein uberjar
java -jar target/gamegyft-api-0.1.0-SNAPSHOT-standalone.jar
```


## Creation of the Java Keystores
*please review dev/keys/Makefile*
```bash
cd dev/keys
make java-keystore
cp keystore.jks ../../resources
```
