# stateServer

This application creates a server at localhost:8080. The server takes in two url paramaters, longitude and latitude, and finds the state corresponding to that location.

The code for server is not robust and uses Java's HttpServer library. It assumes two parameters are sent and first one is always longitute and the second is latitude. The server can be made more robust using a full featured RESTful library. 

#### Running the Server:
1. Navigate to `src` directory
2. Run command `make`

#### Testing the Server:
1. Run command `curl  -d "longitude=-77.036133&latitude=40.513799" http://localhost:8080/`
2. The output should be the state which has a location with that longitude and latitude. In this case it is Pennsylvania.

#### Notes:
I have modified the states.json file because it was not in the correct json format. No changes were made to the data but the structure of json was reformatted.
