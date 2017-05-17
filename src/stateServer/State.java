package stateServer;

import java.util.ArrayList;

/**
 * Created by shishir on 5/13/2017.
 * lib.stateServer.State has list of borders which defines the boundries of the state
 */
public class State {

    String name;
    ArrayList<Border> borders;

    public State(String name){
        this.name = name;
        borders = new ArrayList<Border>();
    }

    public void addBorder(Border border){
        borders.add(border);
    }

    public boolean checkHorizantal(Point point){
        for(Border border: borders){
            Double high = border.start.lat;
            Double low = border.end.lat;
            //checking which one is higher
            if(border.start.lat < border.end.lat){
                high = border.end.lat;
                low = border.start.lat;
            }
            if(low <= point.lat && point.lat <= high){
                return true;
            }
        }
        return false;
    }

    public boolean checkVertical(Point point){
        for(Border border: borders){
            Double high = border.start.lon;
            Double low = border.end.lon;
            //checking which one is higher
            if(border.start.lon < border.end.lon){
                high = border.end.lon;
                low = border.start.lon;
            }
            if(low <= point.lon && point.lon <= high){
                return true;
            }
        }
        return false;
    }
}
