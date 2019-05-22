Proxy
---------------------
This is a proxy rest api

Description
-----------
- Proxy api provides endpoint to login user
- Proxy api provides endpoints to search suburb by post code and name, and also allows user to add new suburb details
- Proxy api runs on port 8081.
- Proxy api run as a EurekaClient and serve as api gateway.
- Proxy api contains public and private endpoints, where private endpoint requires JWT to access. Header value Authorization: Bearer <JWT Token> is required for private endpoint.
- Spring security has been used for authentication and authorisation.
- user01 has been added to use this application with required role.


Endpoint Detail
-----------
- Endpoint /public/api/login receives json {"userName": "user01", "password": "some-password" } and return JWT token
- Endpoint /public/api/postcode/{postCode} return Suburb detail using post code
- Endpoint /public/api/name/{name} return Suburb detail using name
- Endpoint /private/api/add adds suburb detail


## Compile, Test, Run and Packaging

- Compile: mvn compile

- Test: mvn test

- Packaging: mvn package, compiled jar in *target/* folder

- Run using jar: java -jar proxy-0.0.1-SNAPSHOT.jar from *target/* folder

