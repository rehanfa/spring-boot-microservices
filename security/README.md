Security
---------------------
This is a Security api project

Description
-----------
- Security api provides authentication and authorisation services.
- Security api runs on port 8090.
- Security api run as a EurekaClient
- Security api is designed to access through Proxy api.

Endpoint Detail
-----------
- login endpoint /api/login receives json {"userName": "user01", "password": "som-password" } and return JWT token
- hasAccess endpoint /api/{token}/{service} validates the access
 
