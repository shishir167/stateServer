package stateServer;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;


/**
 * Created by shishir on 5/13/2017.
 */
public class Master {

    static ArrayList<State> states = new ArrayList<State>();

    public static void main(String[] args){
        readJsonAndPopulateStates();
        startServer();
    }

    private static void startServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/", new RequestHandler());
        server.start();
    }


    static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String answer = in.readLine();

            //This assumes two parameters are sent and first one is longitute and the second is latitude
            //This can be made more robust using a full featured RESTful library
            String[] parameters = answer.split("&");
            Double lat = new Double(parameters[0].split("=")[1]);
            Double lon = new Double(parameters[1].split("=")[1]);
//            System.out.println("lat: " + lat + ", lon: " + lon);

            State answerState = checkCoordinate(lat, lon);
            String response = "";
            if (answerState == null){
//                System.out.println("No state found with that coordinate");
                response = "No state found with that coordinate" + "\n";
            }
            else {
//                System.out.println(answerState.name);
                response = answerState.name + "\n";
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static void readJsonAndPopulateStates() {
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("states.json"));
            JsonObject jsonObject = new JsonParser().parse(jsonReader).getAsJsonObject();
            JsonArray array = (JsonArray)jsonObject.get("json");
            State state;
            for(Object ob: array){
                JsonObject jo = (JsonObject)ob;
                String stateName = jo.get("state").toString().replace("\"", "");
                state = new State(stateName);
                JsonArray border = (JsonArray)jo.get("border");
                JsonArray singleCoordinates = (JsonArray)border.get(0);
                Double lat = Double.valueOf(singleCoordinates.get(0).toString());
                Double lon = Double.valueOf(singleCoordinates.get(1).toString());
                Point oldPoint = new Point(lat, lon);
                for(int i = 1; i<border.size(); i++){
                    singleCoordinates = (JsonArray)border.get(i);
                    lat = Double.valueOf(singleCoordinates.get(0).toString());
                    lon = Double.valueOf(singleCoordinates.get(1).toString());
                    Point currentPoint = new Point(lat, lon);
                    Border currentBorder = new Border(oldPoint, currentPoint);
                    state.addBorder(currentBorder);
                }
                states.add(state);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*
    This function finds the state corresponding to the location.
    If a coordinate's both latitude and longitude falls insides the boundries of a State it must be in the state.
     */
    public static State checkCoordinate(Double lat, Double lon){
        for(State state: states){
            if(state.isLongitudeInsideState(lon) && state.isLatitudeInsideState(lat)){
                return state;
            }
        }
        return null;
    }
}
