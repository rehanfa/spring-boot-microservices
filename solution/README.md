Solution
---------------------
This is a service rest api

Description
-----------
- Solution api provides endpoints to search suburb by post code and name, and also allows user to add new suburb details
- Solution api runs on port 8091.
- Solution api run as a EurekaClient.
- Solution api is designed to access through Proxy api.


Endpoint Detail
-----------
- Endpoint /api/postcode/{postCode} return Suburb detail using post code
- Endpoint /api/name/{name} return Suburb detail using name
- Endpoint /api/add adds suburb detail
- H2 in memory database is used as a database
- Melbourne  details has been added as a inital data
- Sample Subburb detail is:
- {
    "name": "Geelong",
    "postCode": 3220,
    "state": {
        "stateId": 10,
        "name": "Victoria",
        "abbreviation": "VIC"
    },
    "location": {
        "locationId": null,
        "locality": "VIC COUNTRY",
        "latitude": -38.1583,
        "longitude": 144.35
    }
}


## Requirements

- Implemented and tested using Java 8

- Tests require JUnit and Mockito

- Project dependencies and compiling managed by Maven


## Compile, Test, Run and Packaging

- Compile: mvn compile

- Test: mvn test

- Packaging: mvn package, compiled jar in *target/* folder

- Run using jar: java -jar solution-0.0.1-SNAPSHOT.jar from *target/* folder
 
